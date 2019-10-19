package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.messengerapp.fragments.ChatFragment;
import com.example.messengerapp.fragments.MoreFragment;
import com.example.messengerapp.fragments.PeopleFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ChatFragment mChatFragment = new ChatFragment();
    private PeopleFragment mPeopleFragment = new PeopleFragment();
    private MoreFragment mMoreFragment = new MoreFragment();
    FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar_main;
    CircleImageView profile_image;
    private DocumentReference currentUserDocRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar_main         = findViewById(R.id.toolbar_main);
        profile_image        = findViewById(R.id.profile_image);
        currentUserDocRef    = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setFragment(mChatFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_chat_item){
                    setFragment(mChatFragment);
                    return true;
                }
                else if (id == R.id.navigation_people_item){
                    setFragment(mPeopleFragment);
                    return true;
                }
                else if (id == R.id.navigation_more_item){
                    setFragment(mMoreFragment);
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        currentUserDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String image_uri = documentSnapshot.getString("picture");
                if (image_uri != null && !image_uri.isEmpty()) {
                    Picasso.with(MainActivity.this).load(image_uri).into(profile_image);
                }else {
                    profile_image.setImageResource(R.drawable.ic_account_circle);
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.coordinatorLayout,fragment).addToBackStack(null).commit();
    }

}
