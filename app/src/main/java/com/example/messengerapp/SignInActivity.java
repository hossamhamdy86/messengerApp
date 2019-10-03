package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements TextWatcher {

    Button btn_registration , btn_login_sign_in;
    EditText email_sign_in , password_sign_in;
    String email , password;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener  mAuthListener;
    ProgressBar progressBar_Sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_registration = findViewById(R.id.registration);
        btn_login_sign_in = findViewById(R.id.login_sign_in);
        email_sign_in = findViewById(R.id.email_sign_in);
        password_sign_in = findViewById(R.id.password_sign_in);
        progressBar_Sign_in = findViewById(R.id.progressBar_Sign_in);
        email_sign_in.addTextChangedListener(this);
        password_sign_in.addTextChangedListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        email = email_sign_in.getText().toString().trim();
        password = password_sign_in.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            btn_login_sign_in.setEnabled(false);
        }else {
            btn_login_sign_in.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void greateNewAccount(View view) {
        Intent intent_signIn = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent_signIn);
    }

    public void signIn(View view) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_sign_in.setError("please enter a valid Email");
            email_sign_in.requestFocus();
        }
        else if (password.length() < 6) {
            password_sign_in.setError("password must be at least 6 char");
            password_sign_in.requestFocus();
        }else {
            progressBar_Sign_in.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressBar_Sign_in.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        progressBar_Sign_in.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}


