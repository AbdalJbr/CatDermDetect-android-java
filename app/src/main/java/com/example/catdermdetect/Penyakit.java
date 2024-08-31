package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.catdermdetect.HelperClasses.HomeAdapter.PenyakitAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Penyakit extends AppCompatActivity {

    TextView title, detail;
    FirebaseFirestore db;
    FirebaseStorage storage;
    ImageView imgPenyakit;
    RecyclerView recyclerView;
    PenyakitAdapter adapter;
    private List<ListPenyakit> penyakitList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit);

        title = findViewById(R.id.tittle);
        detail = findViewById(R.id.detail);
        imgPenyakit = findViewById(R.id.imgPenyakit);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PenyakitAdapter(penyakitList);
        recyclerView.setAdapter(adapter);

        searchData();


    }
    public void searchData(){
        db.collection("penyakit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()){
                                ListPenyakit penyakit = document.toObject(ListPenyakit.class);
                                penyakitList.add(penyakit);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Log.w("Penyakit", "Error get Data from DB.", task.getException());
                        }
                    }
                });

    }





}