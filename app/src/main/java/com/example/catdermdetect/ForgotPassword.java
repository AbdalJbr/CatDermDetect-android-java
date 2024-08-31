package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextInputLayout layEmail;
    TextInputEditText email;
    Button btnKirim;
    TextView notif;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        layEmail = findViewById(R.id.layEmail);
        email = findViewById(R.id.email);
        btnKirim = findViewById(R.id.btnKirim);
        notif = findViewById(R.id.emailSent);

        mAuth = FirebaseAuth.getInstance();
        final CustomLoading customLoading = new CustomLoading(ForgotPassword.this);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                if (getEmail.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Tolong diisi Email nya", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
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
                                            notif.setVisibility(View.VISIBLE);
                                        }
                                    },3000);
                                    email.setText("");
                                }else{
                                    Toast.makeText(ForgotPassword.this, "Cek Email , apakah Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}