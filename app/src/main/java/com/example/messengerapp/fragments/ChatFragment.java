package com.example.messengerapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.messengerapp.Adapter.Adapter_user_item;
import com.example.messengerapp.Notifications.token;
import com.example.messengerapp.R;
import com.example.messengerapp.model.Users_item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    TextView title_toolbar_textview;

    RecyclerView recyclerView_userItem ;

    ArrayList<Users_item> usersItemArrayList = new ArrayList<Users_item>();

    Adapter_user_item adapter_user_item;

    private CollectionReference currentUserDocRef;

    ProgressBar progressBar_chat_fragment;

    private DocumentReference currentUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView_userItem = (RecyclerView) view.findViewById(R.id.recyclerView_chat);
        progressBar_chat_fragment = view.findViewById(R.id.progressBar_chat_fragment);
        title_toolbar_textview = getActivity().findViewById(R.id.title_toolbar_textview);
        title_toolbar_textview.setText("Chats");
        recyclerView_userItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentUser = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        currentUserDocRef = FirebaseFirestore.getInstance().collection("Users");
        currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar_chat_fragment.setVisibility(View.VISIBLE);
                    String currentUserId = currentUser.getId();
                    usersItemArrayList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            Users_item item = new Users_item();
                            item.setName_recycler(document.getString("name"));
                            item.setImage_recycler(document.getString("picture"));
                            item.setuId(userId);
                            if (currentUserId == userId){
                                break;
                            }else {
                                usersItemArrayList.add(item);
                                Log.e("size", usersItemArrayList.size() + "") ;
                            }
                        }
                    progressBar_chat_fragment.setVisibility(View.INVISIBLE);
                    adapter_user_item = new Adapter_user_item(getContext() , usersItemArrayList);
                    recyclerView_userItem.setAdapter(adapter_user_item);
                    }
            }
        });

        return view;
    }



}
