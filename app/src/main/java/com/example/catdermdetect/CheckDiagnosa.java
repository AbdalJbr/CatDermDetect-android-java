package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.catdermdetect.HelperClasses.HomeAdapter.GejalaAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CheckDiagnosa extends AppCompatActivity {

    RecyclerView recyclerView;
    GejalaAdapter adapter;
    FirebaseFirestore db;
    FirebaseStorage storage;
    CheckBox cekGejala;
    Button btnCekHasil;
    private List<ListGejala> gejalaList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_diagnosa);

        cekGejala = findViewById(R.id.cekGejala);
        btnCekHasil = findViewById(R.id.btnHasil);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView_diagnosa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GejalaAdapter(gejalaList);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String receivedDataUsername = intent.getStringExtra("username");
        String receivedDataEmail = intent.getStringExtra("useremail");

        getData();

        btnCekHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedGejala = new ArrayList<>();
                for (ListGejala g : gejalaList) {
                    if (g.isChecked()) {
                        selectedGejala.add(g.getKd_gejala());
                    }
                }

                for (String gejala : selectedGejala) {
                    Log.d("CheckDiagnosa", "Selected Gejala: " + gejala);
                }

                if (!selectedGejala.isEmpty()) {
                    Intent move_hasil = new Intent(CheckDiagnosa.this, HasilDiagnosa.class);
                    move_hasil.putExtra("username",receivedDataUsername);
                    move_hasil.putExtra("useremail",receivedDataEmail);
                    Log.d("CEK VARIABEL","note : " +receivedDataEmail +""+receivedDataUsername);
                    move_hasil.putStringArrayListExtra("selectedGejala", (ArrayList<String>) selectedGejala);
                    startActivity(move_hasil);
                } else {
                    Toast.makeText(CheckDiagnosa.this, "Pilih gejala terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData() {
        db.collection("gejala")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                ListGejala gejala = document.toObject(ListGejala.class);
                                gejalaList.add(gejala);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Log.w("Gejala", "Error get data from DB", task.getException());
                        }
                    }
                });
    }

}