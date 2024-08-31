package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 5000;
    Animation topAnimate, botAnimate;
    ImageView icon;
    TextView textLogo, slogan;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        topAnimate = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnimate = AnimationUtils.loadAnimation(this,R.anim.bot_animation);

        icon = findViewById(R.id.icon);
        textLogo = findViewById(R.id.textLogo);
        slogan = findViewById(R.id.slogan);

        icon.setAnimation(topAnimate);
        textLogo.setAnimation(botAnimate);
        slogan.setAnimation(botAnimate);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            String userEmail = user.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("Email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                                startActivity(intent);
                                finish();
                            } else {
                                FirebaseAuth.getInstance().signOut();
                                Intent move = new Intent(MainActivity.this, Login.class);

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View,String>(icon, "icon_image");
                                pairs[1] = new Pair<View,String>(textLogo, "transitionLogo");

                                ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                                startActivity(move, option.toBundle());
                                finish();
                            }
                        }
                    });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent move = new Intent(MainActivity.this, Login.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View,String>(icon,"icon_image");
                    pairs[1] = new Pair<View,String>(textLogo,"transitionLogo");


                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(move, option.toBundle());

                }
            },SPLASH_SCREEN);

        }

    }
    @Override
    public void onBackPressed() {

    }
}