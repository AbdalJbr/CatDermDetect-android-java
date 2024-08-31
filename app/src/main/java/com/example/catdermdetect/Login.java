package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Pair;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 5000;
    ImageView icon;
    TextView logo,textSlogan,textInfo ;
    TextInputEditText fEmail, fPassword;
    TextInputLayout layEmail, layPass;
    Button btnLogin, btnSignUp, btnForgotPass;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        icon = findViewById(R.id.icon);
        logo = findViewById(R.id.logo);
        textSlogan = findViewById(R.id.textSlogan);
        textInfo = findViewById(R.id.textInfo);
        fEmail = findViewById(R.id.email);
        fPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        btnForgotPass = findViewById(R.id.btnForgotPass);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final CustomLoading customLoading = new CustomLoading(Login.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = fEmail.getText().toString();
                password = fPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    customLoading.startLoading();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            customLoading.endLoading();
                                            Toast.makeText(Login.this, "Login Is Successfully", Toast.LENGTH_SHORT).show();
                                            Intent move = new Intent(Login.this, HomeScreen.class);
                                            startActivity(move);
                                        }
                                    },3000);

                                } else {
                                    Toast.makeText(Login.this, "Login Failed\nPlease check again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(Login.this, Registration.class);

                Pair [] pairs = new Pair[8];
                pairs[0] = new Pair<View,String>(icon,"iconImage");
                pairs[1] = new Pair<View,String>(logo,"transitionLogo");
                pairs[2] = new Pair<View,String>(textSlogan,"transitionSlogan");
                pairs[3] = new Pair<View,String>(textInfo,"transitionInfo");
                pairs[4] = new Pair<View,String>(fEmail,"transitionUsername");
                pairs[5] = new Pair<View,String>(fPassword,"transitionPassword");
                pairs[6] = new Pair<View,String>(btnLogin,"transitionBtnLogin");
                pairs[7] = new Pair<View,String>(btnSignUp,"transitionBtnSignUp");

                ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(move, option.toBundle());
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(Login.this, ForgotPassword.class);
                startActivity(move);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you want to get out from CatDermDetect ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent closeApp = new Intent(Intent.ACTION_MAIN);
                        closeApp.addCategory(Intent.CATEGORY_HOME);
                        closeApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(closeApp);
                        finishAffinity();
                    }
                })
                .setNegativeButton("No, Im Stay", null)
                .show();
    }


}