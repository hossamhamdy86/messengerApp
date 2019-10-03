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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ChatFragment mChatFragment = new ChatFragment();
    private PeopleFragment mPeopleFragment = new PeopleFragment();
    private MoreFragment mMoreFragment = new MoreFragment();
    FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar_main;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setFragment(mChatFragment);
        toolbar_main = findViewById(R.id.toolbar_main);
        profile_image =(ImageView) findViewById(R.id.profile_image);
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
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Sign_out(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.coordinatorLayout,fragment).addToBackStack(null).commit();
    }


}
