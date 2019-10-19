package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.messengerapp.Adapter.messageAdapter;
import com.example.messengerapp.model.chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageActivity extends AppCompatActivity {

    ImageView image_send_message ;
    EditText edit_text_send_message;
    FirebaseUser fuser =FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestoreInstant = FirebaseFirestore.getInstance();
    CollectionReference chatChannelCollectionRef = firestoreInstant.collection("messageChannel");
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference newchatchannel = firestoreInstant.collection("Users").document();
    TextView title_toolbar_textview;
    CircleImageView profile_image;
    String userid;
    DocumentReference currentUserDocRef ;

    messageAdapter messageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        title_toolbar_textview  = findViewById(R.id.title_toolbar_textview);
        profile_image           = findViewById(R.id.profile_image);
        image_send_message      = findViewById(R.id.image_send_message);
        edit_text_send_message  = findViewById(R.id.edit_text_send_message);
        recyclerView            = findViewById(R.id.recyclerView);
        currentUserDocRef       = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Intent intent = getIntent();
        title_toolbar_textview.setText(intent.getStringExtra("name"));
        if (intent.getStringExtra("picture") != null && !intent.getStringExtra("picture").isEmpty()) {
            Picasso.with(this).load(intent.getStringExtra("picture")).into(profile_image);
        }else {
            profile_image.setImageResource(R.drawable.ic_account_circle);
        }
        userid = intent.getStringExtra("Uid");

        greateChat();

        readmessage(fuser.getUid(),userid , intent.getStringExtra("picture"));

        image_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edit_text_send_message.getText().toString();
                if (!message.equalsIgnoreCase("")){
                    sendmesage(fuser.getUid(),userid,message,Calendar.getInstance().getTime());
                    readmessage(fuser.getUid(),userid , intent.getStringExtra("picture"));
                }
                edit_text_send_message.setText("");
            }
        });
    }

    private void sendmesage(final String sender , final String recriver , final String message , final Date date){
        firestoreInstant.collection("Users").document(currentUserId)
                .collection("chatChannel").document(userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String msgId = task.getResult().getString("channalId");
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("sender",sender);
                hashMap.put("recriver",recriver);
                hashMap.put("message",message);
                hashMap.put("Date",date);
                chatChannelCollectionRef.document(msgId).collection("message").add(hashMap);
            }
        });
    }

    private void greateChat() {
        firestoreInstant.collection("Users").document(currentUserId)
                .collection("chatChannel").document(userid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    return;
                }else {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("channalId",newchatchannel.getId());

                    firestoreInstant.collection("Users").document(userid)
                            .collection("chatChannel").document(currentUserId)
                            .set(hashMap);

                    firestoreInstant.collection("Users").document(currentUserId)
                            .collection("chatChannel").document(userid)
                            .set(hashMap);
                }
            }
        });
    }

    private void readmessage(final String myid , final String userid , final String imageuri){
        firestoreInstant.collection("Users").document(currentUserId)
                .collection("chatChannel").document(userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String msgId = task.getResult().getString("channalId");
                if (msgId == null){
                    return;
                }else {
                CollectionReference collectionReference = firestoreInstant.collection("messageChannel")
                        .document(msgId)
                        .collection("message");
                    collectionReference.orderBy("Date").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                final ArrayList<chat> mchat = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                                    chat chat = document.toObject(chat.class);
                                    Log.e("path",chat.getMessage());
                                    mchat.add(chat);
                                }
                                currentUserDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String CurrentUser_image = documentSnapshot.getString("picture");
                                        messageAdapter = new messageAdapter(messageActivity.this,mchat,CurrentUser_image,imageuri);
                                        recyclerView.setAdapter(messageAdapter);
                                    }
                                });
                            }
                        });
                }
            }
        });

    }
}
