package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.catdermdetect.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.example.catdermdetect.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView tvName, tvEmail;
    View headerView;
    Toolbar toolbar;
    ImageSlider imgSlider;
    RecyclerView featuredRecyler;
    RecyclerView.Adapter adapter;
    ImageView edukasi, profile, penyakit, obatVit, riwayat, checkDiagnosa;
    FirebaseUser user;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        edukasi = findViewById(R.id.edukasi);
        penyakit = findViewById(R.id.penyakit);
        obatVit = findViewById(R.id.obatVit);
        riwayat = findViewById(R.id.riwayat);
        checkDiagnosa = findViewById(R.id.checkDiagnosa);
        headerView = navigationView.getHeaderView(0);
        tvName= headerView.findViewById(R.id.username);
        tvEmail= headerView.findViewById(R.id.email);
        profile = headerView.findViewById(R.id.profileImg);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        featuredRecyler = findViewById(R.id.banner_recycler);
        featuredRecycler();

        imageSlider();
        menuScreen();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


    }

    public void menuScreen(){
        checkDiagnosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_checkDiagnosa = new Intent(HomeScreen.this, CheckDiagnosa.class);
                String username = tvName.getText().toString().trim();
                String useremail = tvEmail.getText().toString().trim();
                move_checkDiagnosa.putExtra("username",username);
                move_checkDiagnosa.putExtra("useremail",useremail);
                startActivity(move_checkDiagnosa);
            }
        });

        edukasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_edukasi = new Intent(HomeScreen.this, Education.class);
                startActivity(move_edukasi);
            }
        });

        penyakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_penyakit = new Intent(HomeScreen.this, Penyakit.class);
                startActivity(move_penyakit);
            }
        });

        obatVit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_obatVit = new Intent(HomeScreen.this, ObatVit.class);
                startActivity(move_obatVit);
            }
        });

        riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getText().toString();
                Intent move_riwayat = new Intent(HomeScreen.this, Riwayat.class);
                move_riwayat.putExtra("email",email);
                startActivity(move_riwayat);
            }
        });



    }

    public void imageSlider(){

        imgSlider = findViewById(R.id.img_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1 , ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2 , ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3 , ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide4 , ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide5 , ScaleTypes.FIT));

        imgSlider.setImageList(slideModels, ScaleTypes.FIT);
    }

    private void featuredRecycler(){
        featuredRecyler.setHasFixedSize(true);
        featuredRecyler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelperClass(R.drawable.catspluff, "Cat Spluff","Relaksasi yang menyenangkan, merasa aman, bahagia dan rileks"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.catloaf, "Cat Loaf","Menghemat panas, rileks dan nyaman"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.crouching, "Crouching Semi-Loaf","Waspada dan siap menerkam"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.tightcurl, "Tight Curl","Menghemat panas, tenang dan bahagia dekat dengan kita"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.sidesleep, "Side Sleeping","Beristirahat atau merasa sedikit kepanasan dan butuh pendinginan"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.snugglingup, "Snuggling Up","Mencintai dan menginginkan kasih sayang"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.themonorail, "The Monorail","Sangat tenang tanpa ada keinginan untuk waspada"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.headpress, "Head Press","Memungkinkan kucing merasa sakit"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecyler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            searchEmail();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent moveProfile = new Intent(HomeScreen.this, Profile.class);
                moveProfile.putExtra("key", tvEmail.getText().toString());
                startActivity(moveProfile);
                break;
            case R.id.nav_contactUs:
                Intent moveContactus = new Intent(HomeScreen.this, ContactUs.class);
                startActivity(moveContactus);
                break;
            case R.id.nav_logout:
                Intent moveSettings = new Intent(HomeScreen.this, Settings.class);
                startActivity(moveSettings);
                break;
        }

        return true;
    }

    public void searchEmail(){
        String userEmail = user.getEmail();
        db.collection("users")
                .whereEqualTo("Email",userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String getName = document.getString("Fullname");
                            String getEmail = document.getString("Email");
                            String gImgProfile = document.getString("imageUrl");

                            tvName.setText(getName);
                            tvEmail.setText(getEmail);
                            Glide.with(HomeScreen.this)
                                    .load(gImgProfile)
                                    .into(profile);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeScreen.this, "Failed to get Username" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}