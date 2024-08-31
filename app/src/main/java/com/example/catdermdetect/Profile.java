package com.example.catdermdetect;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private AlertDialog progressDialog;
    Button btnBack, btnEdit, btnEdUsername, btnUsername, btnChangeProfile, btnReset;
    TextView tvUsername;
    TextInputEditText tedEmail, tedMoPhone, tedPassword, tedRepassword ;
    TextInputLayout telEmail, telMoPhone, telPassword, telRepassword;
    LinearLayout parentLay;
    View vFirst, vSecond;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView profileImgView;
    Uri imgUri;
    StorageReference storageReference, desertReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnEdit = findViewById(R.id.btnEdit);
        btnEdUsername = findViewById(R.id.btnEdUsername);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);
        profileImgView = findViewById(R.id.imgProfile);
        parentLay = findViewById(R.id.parent);
        tvUsername = parentLay.findViewById(R.id.username);

        btnUsername = parentLay.findViewById(R.id.btnEdUsername);
        btnChangeProfile = parentLay.findViewById(R.id.btnChangeProfile);
        tedEmail = parentLay.findViewById(R.id.email);
        tedMoPhone = parentLay.findViewById(R.id.moPhone);
        telEmail = parentLay.findViewById(R.id.layEmail);
        telMoPhone = parentLay.findViewById(R.id.layMoPhone);
        btnReset = parentLay.findViewById(R.id.btnReset);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profileImage");
        mAuth = FirebaseAuth.getInstance();
        searchUser();

        final CustomLoading customLoading = new CustomLoading(Profile.this);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = tedEmail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(getEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    customLoading.startLoading();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            customLoading.endLoading();
                                            Toast.makeText(Profile.this, "Cek Email untuk Reset Password", Toast.LENGTH_SHORT).show();
                                        }
                                    },3000);
                                }else{
                                    Toast.makeText(Profile.this, "Maaf, ada kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();

            }
        });

        btnEdUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
            }
        });

        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void deleteProfile(){
        String getEmail = getIntent().getStringExtra("key");
        if (getEmail != null) {
            db.collection("users")
                    .whereEqualTo("Email", getEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String getIdImgProfile = "profileImage/bg_default_profile.jpg";

                                Glide.with(Profile.this)
                                        .load(getIdImgProfile)
                                        .apply(new RequestOptions().circleCrop()
                                                .placeholder(R.drawable.skinnycats__1_)
                                                .error(R.drawable.ic_error))
                                        .into(profileImgView);
                                saveUrlImage();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Failed to get User Data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Profile.this, "Email not provided", Toast.LENGTH_SHORT).show();
        }

    }
    public void openFileChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        builder.setPositiveButton("Choose File", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        builder.setNeutralButton("Delete Profile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProfile();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            UCrop.Options options = new UCrop.Options();
            options.setCompressionQuality(70);
            options.setCircleDimmedLayer(true);
            options.setShowCropGrid(false);
            options.setHideBottomControls(true);
            options.setFreeStyleCropEnabled(true);
            UCrop.of(imgUri, Uri.fromFile(new File(getCacheDir(), "cropped_image")))
                    .withAspectRatio(1, 1)
                    .withOptions(options)
                    .start(this);
            uploadImage();
            saveUrlImage();

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {
                Glide.with(this)
                        .load(resultUri)
                        .into(profileImgView);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

    }

    private void uploadImage() {
        if (imgUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imgUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(Profile.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Glide.with(Profile.this)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().circleCrop()
                                        .placeholder(R.drawable.skinnycats__1_)
                                        .error(R.drawable.ic_error))
                                    .into(profileImgView);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(Profile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUrlImage(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String getEmail = tedEmail.getText().toString();

        db.collection("users")
                .whereEqualTo("Email", getEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        String userId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("imageUrl", imgUri);
                        db.collection("users").document(userId)
                                .update(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Imaged Save", "Document successfully Saved!");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("Image Save", "Document Failed to save!");
                                });
                    } else {
                        Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Profile.this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void editUser(){
        String  nEmail, nMoPhone, nPassword, nRepassword;
        nEmail = tedEmail.getText().toString();
        nMoPhone = tedMoPhone.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        final EditText inputNewMobPhone = dialogView.findViewById(R.id.input_new_mob_phone);

        inputNewMobPhone.setText(nMoPhone);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mobPhone = inputNewMobPhone.getText().toString();

                final CustomLoading customLoading = new CustomLoading(Profile.this);

                Map<String, Object> users = new HashMap<>();
                users.put("Phone", mobPhone);

                db.collection("users")
                        .whereEqualTo("Email",nEmail)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()){

                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();
                                    db.collection("users")
                                            .document(documentID)
                                            .update(users)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    customLoading.startLoading();

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            customLoading.endLoading();
                                                            Toast.makeText(Profile.this, "Successfully Update Profile", Toast.LENGTH_SHORT).show();
                                                            searchUser();
                                                        }
                                                    },3000);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Profile.this, "Failure Update Profile\n" + e, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else {
                                    Toast.makeText(Profile.this, "Error" + System.err, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void updateUsername(){
        String nUsername = tvUsername.getText().toString();
        String keyEmail = tedEmail.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Username");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(nUsername);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();
                final CustomLoading customLoading = new CustomLoading(Profile.this);

                Map<String, Object> users = new HashMap<>();
                users.put("Fullname", newUsername);

                db.collection("users")
                        .whereEqualTo("Email", keyEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()){
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();
                                    db.collection("users")
                                            .document(documentID)
                                            .update(users)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    customLoading.startLoading();

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            customLoading.endLoading();
                                                            Toast.makeText(Profile.this, "Successfully Update Username", Toast.LENGTH_SHORT).show();
                                                            searchUser();
                                                        }
                                                    },3000);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Profile.this, "Failure Update Username\n" + e, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else {
                                    Toast.makeText(Profile.this, "Error" + System.err, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    public void searchUser(){
        String getEmail = getIntent().getStringExtra("key");
        db.collection("users")
                .whereEqualTo("Email",getEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String gName = document.getString("Fullname");
                            String gEmail = document.getString("Email");
                            String gMoPhone = document.getString("Phone");
                            String gImgProfile = document.getString("imageUrl");


                            tvUsername.setText(gName);
                            tedEmail.setText(gEmail);
                            tedMoPhone.setText(gMoPhone);

                            Glide.with(Profile.this)
                                    .load(gImgProfile)
                                    .apply(new RequestOptions().circleCrop()
                                            .placeholder(R.drawable.skinnycats__1_)
                                            .error(R.drawable.ic_error))
                                        .into(profileImgView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Failed to get Data User" + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}