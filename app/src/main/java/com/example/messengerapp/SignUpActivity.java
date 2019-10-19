package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {

  TextView edit_text_name, edit_text_email, edit_text_password;
  Button btn_sign_up;
  ProgressBar progressBar_sign_up;
  String name, email, password;
  FirebaseAuth mAuth = null;
  String UId;
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  private DocumentReference curruntUserDocRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    edit_text_name = findViewById(R.id.edit_text_name_sign_up);
    edit_text_email = findViewById(R.id.edit_text_email_sign_up);
    edit_text_password = findViewById(R.id.edit_text_password_sign_up);
    btn_sign_up = findViewById(R.id.btn_sign_up);
    progressBar_sign_up = findViewById(R.id.progressBar_sign_up);
    mAuth = FirebaseAuth.getInstance();
    edit_text_name.addTextChangedListener(this);
    edit_text_email.addTextChangedListener(this);
    edit_text_password.addTextChangedListener(this);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    name = edit_text_name.getText().toString().trim();
    email = edit_text_email.getText().toString().trim();
    password = edit_text_password.getText().toString().trim();
    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
      btn_sign_up.setEnabled(false);
    }else {
      btn_sign_up.setEnabled(true);
    }
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }

  public void registration(View view) {
    if (name.isEmpty()) {
      edit_text_name.setError("please enter your name");
      edit_text_name.requestFocus();
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      edit_text_email.setError("please enter a valid Email");
      edit_text_email.requestFocus();
    } else if (password.length() < 6) {
      edit_text_password.setError("password must be at least 6 char");
      edit_text_password.requestFocus();
    } else {
      progressBar_sign_up.setVisibility(View.VISIBLE);
      mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          curruntUserDocRef = firestore.collection("Users").document(mAuth.getCurrentUser().getUid());
          Map<String,Object> newUser = new HashMap<String, Object>();
          newUser.put("name",name);
          newUser.put("picture","");
          curruntUserDocRef.set(newUser);
          if (task.isSuccessful()) {
            progressBar_sign_up.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
          } else {
            progressBar_sign_up.setVisibility(View.INVISIBLE);
            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }
}
