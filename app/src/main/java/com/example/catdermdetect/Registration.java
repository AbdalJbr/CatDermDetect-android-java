package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    Button btnLogin, btnSignUp;
    TextInputEditText edFName, edLName, edEmail, edPhone, edPassword, edRepassword;
    TextView noIndo;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSignUp = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnAlready);
        edFName = findViewById(R.id.fName);
        edLName = findViewById(R.id.lName);
        edEmail = findViewById(R.id.email);
        edPhone = findViewById(R.id.noPhone);
        edPassword = findViewById(R.id.password);
        edRepassword = findViewById(R.id.repassword);
        noIndo = findViewById(R.id.indo);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(Registration.this, Login.class);
                startActivity(move);
            }
        });

        final CustomLoading customLoading = new CustomLoading(Registration.this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName, lName, fullname, email, noDepan, number, phone, password, repassword;
                fName = edFName.getText().toString();
                lName = edLName.getText().toString();
                fullname = fName + " " + lName;
                email = edEmail.getText().toString();
                noDepan = noIndo.getText().toString();
                number = edPhone.getText().toString();
                phone = noDepan + number;
                password = edPassword.getText().toString();
                repassword = edRepassword.getText().toString();

                if (TextUtils.isEmpty(fName)) {
                    Toast.makeText(Registration.this, "Masukkan Nama depan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lName)) {
                    Toast.makeText(Registration.this, "Masukkan nama belakang", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Masukkan Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(Registration.this, "Masukkan nomor Telepon", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Masukkan Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Registration.this, "Masukkan Password Ulang", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(Registration.this, "Password dan Repassword Harus sama", Toast.LENGTH_SHORT).show();
                } else {
                    customLoading.startLoading();
                    checkEmailAndRegister(email, password, fullname, phone, customLoading);
                }
            }
        });
    }

    private void checkEmailAndRegister(String email, String password, String fullname, String phone, CustomLoading customLoading) {
        db.collection("users")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                customLoading.endLoading();
                                Toast.makeText(Registration.this, "Akun kamu sudah ada, coba login ya", Toast.LENGTH_SHORT).show();
                            } else {
                                registerUser(email, password, fullname, phone, customLoading);
                            }
                        } else {
                            customLoading.endLoading();
                            Toast.makeText(Registration.this, "Failed to get Email: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser(String email, String password, String fullname, String phone, CustomLoading customLoading) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserToDatabase(email, fullname, phone, customLoading);
                        } else {
                            customLoading.endLoading();
                            Toast.makeText(Registration.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String email, String fullname, String phone, CustomLoading customLoading) {
        Map<String, Object> users = new HashMap<>();
        users.put("Fullname", fullname);
        users.put("Email", email);
        users.put("Phone", phone);

        db.collection("users")
                .add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        customLoading.endLoading();
                        Toast.makeText(Registration.this, "Registration is Succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customLoading.endLoading();
                        Toast.makeText(Registration.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
