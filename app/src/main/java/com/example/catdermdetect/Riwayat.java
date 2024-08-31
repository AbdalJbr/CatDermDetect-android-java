package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.catdermdetect.HelperClasses.HomeAdapter.RiwayatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Riwayat extends AppCompatActivity {

    TextView id,date;
    Button btnPdf, btnDelete;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RiwayatAdapter adapter;
    private List<ListRiwayat> riwayatList = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        id = findViewById(R.id.id);
        date = findViewById(R.id.date);
        btnPdf = findViewById(R.id.pdf);
        btnDelete = findViewById(R.id.delete);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView_riwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RiwayatAdapter(riwayatList,context);
        recyclerView.setAdapter(adapter);

        getData();
    }

    public void getData(){
        Intent intent =  getIntent();
        String email = intent.getStringExtra("email");
        Log.d("GET DATA RIWAYAT","NOTE : " +email);

        db.collection("diagnosa")
                .whereEqualTo("email",email)
                .orderBy("date",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document:task.getResult()){
                                ListRiwayat riwayat = document.toObject(ListRiwayat.class);
                                riwayatList.add(riwayat);
                                Log.d("RIWAYAT LIST","note : "+riwayatList);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d("Riwayat","Error : "+ task.getException());
                        }
                    }
                });
    }
}