package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catdermdetect.HelperClasses.HomeAdapter.ObatVitAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ObatVit extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    RecyclerView recyclerView;
    ObatVitAdapter adapter;
    private List<ListObatVit> obatVitList = new ArrayList<>();
    ImageView imgObat;
    TextView txtNamaObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat_vit);

        imgObat = findViewById(R.id.imgObat);
        txtNamaObat = findViewById(R.id.txtObatVit);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        recyclerView = findViewById(R.id.recyclerViewObat);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new ObatVitAdapter(this,obatVitList);
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        db.collection("obatVit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()){
                                ListObatVit obat = document.toObject(ListObatVit.class);
                                obatVitList.add(obat);
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }else {
                            Log.w("ObatVit", "Error get Data from DB.", task.getException());
                        }
                    }
                });

    }


}
