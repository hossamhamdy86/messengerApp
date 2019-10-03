package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Empty;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar_profileActivity;
    private static final int PERMISSION_CODE = 100;
    private static final int IMAGE_PICK_CODE = 101;
    private ImageView user_image;
    private FirebaseStorage storageInstance = FirebaseStorage.getInstance();
    private StorageReference currentUserStorageRef = storageInstance.getReference()
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private FirebaseFirestore firestoreInstant = FirebaseFirestore.getInstance();
    private DocumentReference currentUserDocRef = firestoreInstant.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar_profileActivity = findViewById(R.id.toolbar_profileActivity);
        user_image = findViewById(R.id.user_image);
        final ImageView imageView = findViewById(R.id.user_image);
        setSupportActionBar(toolbar_profileActivity);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Me");
        currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                Uri namePic = Uri.parse(documentSnapshot.getString("picture"));
                Toast.makeText(ProfileActivity.this, namePic.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(ProfileActivity.this).load(namePic).into(imageView);
                     }
            }
        });
        user_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(Permissions, PERMISSION_CODE);
                }else {
                    selectimage();
                }
            }
        });
    }
    private void selectimage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_PICK_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    selectimage();
                }else {
                    Toast.makeText(this, "Permission was denied...", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Uri selectedImagePath = data.getData();
            user_image.setImageURI(selectedImagePath);
            uploadProfileImage(selectedImagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileImage(Uri selectedImageByte) {
        final StorageReference reference = currentUserStorageRef.child("profilePicture").child(selectedImageByte.getLastPathSegment());
        reference.putFile(selectedImageByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri imagePath = taskSnapshot.getUploadSessionUri();
                currentUserDocRef.update("picture",imagePath.toString());
            }
        });
    }
}
