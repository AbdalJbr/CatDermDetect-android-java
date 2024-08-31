package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Settings extends AppCompatActivity {

    Button btnDelete, btnLogout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    final CustomLoading customLoading = new CustomLoading(Settings.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnDelete = findViewById(R.id.btnDelete);
        btnLogout = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Konfirmasi Akun");
                builder.setMessage("Apakah kamu yakin ingin menghapus akun ini ?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        customLoading.startLoading();

                        // Meminta kredensial ulang
                        showReauthDialog();

                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
    }

    private void showReauthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_reauth, null);
        builder.setView(customLayout);

        builder.setTitle("Re-authentication");
        builder.setMessage("Please enter your email and password to continue");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText emailInput = customLayout.findViewById(R.id.email);
                EditText passwordInput = customLayout.findViewById(R.id.password);

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    reauthenticateUser(email, password);
                } else {
                    Toast.makeText(Settings.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void reauthenticateUser(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Reauth", "User re-authenticated.");
                            deleteAutentikasi();
                        } else {
                            Log.e("Reauth", "Failed to re-authenticate user.", task.getException());
                            Toast.makeText(Settings.this, "Re-authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation Logout");
        builder.setMessage("Are you sure to logout ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent closeApp = new Intent(Intent.ACTION_MAIN);
                closeApp.addCategory(Intent.CATEGORY_HOME);
                closeApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(closeApp);
                finishAffinity();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAccount() {
        String getEmail = user.getEmail();
        db.collection("users")
                .whereEqualTo("Email", getEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                db.collection("users")
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("DeleteDoc", "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("DeleteDoc", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w("DeleteDocs", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void deleteAutentikasi() {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("DeleteUser", "User account deleted.");
                                deleteAccount();
                                deleteDiagnosa();
                                customLoading.endLoading();
                                Toast.makeText(Settings.this, "Akun Telah terhapus", Toast.LENGTH_SHORT).show();

                                Intent closeApp = new Intent(Intent.ACTION_MAIN);
                                closeApp.addCategory(Intent.CATEGORY_HOME);
                                closeApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(closeApp);
                                finishAffinity();
                            } else {
                                Log.e("DeleteUser", "Failed to delete user account.", task.getException());
                            }
                        }
                    });
        } else {
            Log.d("DeleteUser", "No user is signed in.");
        }
    }

    private void deleteDiagnosa() {
        String getEmail = user.getEmail();
        db.collection("diagnosa")
                .whereEqualTo("email", getEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                db.collection("diagnosa")
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Delete Diagnosa", "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Delete Diagnosa", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w("Delete Diagnosa", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
