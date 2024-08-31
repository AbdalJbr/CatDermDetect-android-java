package com.example.catdermdetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HasilDiagnosa extends AppCompatActivity {

    private static final Map<String, Map<String, Integer>> rule = new HashMap<>();
    static {

        Map<String,Integer> ruleP01 = new HashMap<>();
        ruleP01.put("G01",20);
        ruleP01.put("G02",20);
        ruleP01.put("G03",20);
        ruleP01.put("G04",20);
        ruleP01.put("G05",20);
        rule.put("P01", ruleP01);

        Map<String,Integer> ruleP02 = new HashMap<>();
        ruleP02.put("G06",20);
        ruleP02.put("G07",20);
        ruleP02.put("G08",20);
        ruleP02.put("G09",20);
        ruleP02.put("G10",20);
        rule.put("P02", ruleP02);

        Map<String,Integer> ruleP03 = new HashMap<>();
        ruleP03.put("G06",20);
        ruleP03.put("G07",20);
        ruleP03.put("G09",20);
        ruleP03.put("G11",20);
        ruleP03.put("G12",20);
        rule.put("P03", ruleP03);

        Map<String,Integer> ruleP04 = new HashMap<>();
        ruleP04.put("G06",25);
        ruleP04.put("G07",25);
        ruleP04.put("G08",25);
        ruleP04.put("G13",25);
        rule.put("P04", ruleP04);

        Map<String,Integer> ruleP05 = new HashMap<>();
        ruleP05.put("G06",20);
        ruleP05.put("G07",20);
        ruleP05.put("G08",20);
        ruleP05.put("G11",20);
        ruleP05.put("G13",20);
        rule.put("P05", ruleP05);

        Map<String,Integer> ruleP06 = new HashMap<>();
        ruleP06.put("G14",25);
        ruleP06.put("G15",25);
        ruleP06.put("G16",25);
        ruleP06.put("G17",25);
        rule.put("P06", ruleP06);

        Map<String,Integer> ruleP07 = new HashMap<>();
        ruleP07.put("G07",34);
        ruleP07.put("G10",33);
        ruleP07.put("G18",33);
        rule.put("P07", ruleP07);

        Map<String,Integer> ruleP08 = new HashMap<>();
        ruleP08.put("G07",50);
        rule.put("P08", ruleP08);

        Map<String,Integer> ruleP09 = new HashMap<>();
        ruleP09.put("G05",25);
        ruleP09.put("G19",25);
        ruleP09.put("G20",25);
        ruleP09.put("G21",25);
        rule.put("P09", ruleP09);

        Map<String,Integer> ruleP10 = new HashMap<>();
        ruleP10.put("G22",25);
        ruleP10.put("G23",25);
        ruleP10.put("G24",25);
        ruleP10.put("G25",25);
        rule.put("P10", ruleP10);

    }

    List<String> penyakitTerbesarList = new ArrayList<>();
    double persentaseTerbesar = 0;

    TextView txtPenyakit, txtDeskripsi,txtSolusi;
    ImageView imgPenyakit;
    FirebaseFirestore db;
    Button btnMulaiLagi, btnSelesai;
    String dataUsername, dataEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_diagnosa);

        db = FirebaseFirestore.getInstance();
        txtPenyakit = findViewById(R.id.txtPenyakit);
        txtDeskripsi = findViewById(R.id.txtKeterangan);
        txtSolusi = findViewById(R.id.txtSolusi);
        imgPenyakit = findViewById(R.id.imgPenyakit);
        btnMulaiLagi = findViewById(R.id.btnMulaiLagi);
        btnSelesai = findViewById(R.id.btnSelesai);

        Intent intent = getIntent();
        dataUsername = intent.getStringExtra("username");
        dataEmail = intent.getStringExtra("useremail");
        hitungHasil();

        if (!penyakitTerbesarList.isEmpty()) {
            for (String penyakit : penyakitTerbesarList) {
                Log.d("HasilDiagnosa", "Anda mungkin menderita " + penyakit + " dengan tingkat kecocokan " + persentaseTerbesar + "%");
                getData(penyakit);
            }
        }

        btnMulaiLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_back = new Intent(HasilDiagnosa.this, CheckDiagnosa.class);
                move_back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                move_back.putExtra("username",dataUsername);
                move_back.putExtra("useremail",dataEmail);
                startActivity(move_back);
            }
        });

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHasil();
                Intent move_done = new Intent(HasilDiagnosa.this, HomeScreen.class);
                move_done.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(move_done);
            }
        });


    }

    public void hitungHasil() {
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> selectedGejala = intent.getStringArrayListExtra("selectedGejala");
            if (selectedGejala != null && !selectedGejala.isEmpty()) {

                for (String penyakit : rule.keySet()) {
                    int totalBobot = 0;
                    int totalBobotTerpenuhi = 0;

                    Map<String, Integer> gejalaPenyakit = rule.get(penyakit);
                    for (String gejala : gejalaPenyakit.keySet()) {
                        totalBobot += gejalaPenyakit.get(gejala);
                        if (selectedGejala.contains(gejala)) {
                            totalBobotTerpenuhi += gejalaPenyakit.get(gejala);
                        }
                    }

                    double persentase = ((double) totalBobotTerpenuhi / totalBobot) * 100;
                    if (persentase > 0) {
                        Log.d("HasilDiagnosa", "Persentase kecocokan untuk " + penyakit + ": " + persentase + "%");
                        if (persentase > persentaseTerbesar) {
                            persentaseTerbesar = persentase;
                            penyakitTerbesarList.clear();
                            penyakitTerbesarList.add(penyakit);
                        } else if (persentase == persentaseTerbesar) {
                            penyakitTerbesarList.add(penyakit);
                        }
                    }
                }

            }
        }
    }

    public void getData(String penyakit){
        db.collection("penyakit")
                .whereEqualTo("kd_penyakit",penyakit)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String getNm_penyakit = document.getString("nm_penyakit");
                            String getDeskripsi = document.getString("deskripsi");
                            String getSolusi = document.getString("solusi");

                            txtPenyakit.setText(getNm_penyakit);
                            txtDeskripsi.setText(getDeskripsi);
                            txtSolusi.setText(getSolusi);

                            if (penyakit != null) {
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imgPenyakit/"+penyakit+".jpg");

                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    Log.d("GAMBAR","note "+imageUrl);
                                    Glide.with(HasilDiagnosa.this)
                                            .load(imageUrl)
                                            .apply(new RequestOptions()
                                                    .fitCenter()
                                                    .placeholder(R.drawable.skinnycats__1_)
                                                    .error(R.drawable.ic_error))
                                            .into(imgPenyakit);
                                }).addOnFailureListener(exception -> {
                                    // Handle any errors
                                    Log.e("FirebaseStorage", "Failed to get download URL", exception);
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HasilDiagnosa.this, "Failed to get data" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveHasil(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String id = String.format("%04d%02d%02d%02d%02d%02d", year, month, day, hour, minute, second);
        String username = dataUsername;
        String email = dataEmail;

        Intent intent = getIntent();
        ArrayList<String> selectedGejala = intent.getStringArrayListExtra("selectedGejala");
        String kd_gejala = selectedGejala.toString().trim();

        String kd_penyakit = txtPenyakit.getText().toString().trim();
        String solusi = txtSolusi.getText().toString().trim();
        String date = day + "-" +month +"-"+ year;

        Log.d("TAMPILKAN", "NOTE : "+id+username+email+kd_gejala+kd_penyakit+solusi+date);

        Map<String ,Object> hasilDiag = new HashMap<>();
        hasilDiag.put("id_diagnosa",id);
        hasilDiag.put("username",username);
        hasilDiag.put("email",email);
        hasilDiag.put("kd_penyakit",kd_penyakit);
        hasilDiag.put("kd_gejala",kd_gejala);
        hasilDiag.put("solusi",solusi);
        hasilDiag.put("date",date);

        db.collection("diagnosa")
                .add(hasilDiag)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HasilDiagnosa.this, "Diagnosa sudah selesai dan disimpan pada database\nSilahkan cek di menu riwayat",Toast.LENGTH_SHORT).show();
                        createPdf(id, username, email, kd_gejala, kd_penyakit, solusi, date);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HasilDiagnosa.this, "Data Tidak tersimpan",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void createPdf(String id, String username, String email, String kd_gejala, String kd_penyakit, String solusi, String date){
        String nama_file = id +"_"+username;
        File pdfFile = new File(getExternalFilesDir(null), nama_file +".pdf");

        try {
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdfDoc = new PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cover_top);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ImageData imageData = ImageDataFactory.create(byteArray);
            Image pdfImage = new Image(imageData);
            pdfImage.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(pdfImage);

            Table table = new Table(3);
            table.setHorizontalAlignment(HorizontalAlignment.LEFT);

            // Tambahkan sel-sel ke dalam tabel
            table.addCell(createCell("ID:",""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", id));

            table.addCell(createCell("Username:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", username));

            table.addCell(createCell("Email:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", email));

            table.addCell(createCell("Penyakit:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", kd_penyakit));

            table.addCell(createCell("Gejala:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", kd_gejala));

            table.addCell(createCell("Solusi:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", solusi));

            table.addCell(createCell("Tanggal:", ""));
            table.addCell(createCell(":",""));
            table.addCell(createCell("", date));

            document.add(table);
            document.flush();

            Bitmap bitmap_bot = BitmapFactory.decodeResource(getResources(), R.drawable.cover_bot);
            ByteArrayOutputStream stream_bot = new ByteArrayOutputStream();
            bitmap_bot.compress(Bitmap.CompressFormat.PNG, 100, stream_bot);
            byte[] byteArray_bot = stream_bot.toByteArray();
            ImageData imageData_bot = ImageDataFactory.create(byteArray_bot);
            Image pdfImage_bot = new Image(imageData_bot);
            float maxheight = 100;
            pdfImage_bot.setHeight(maxheight);
            pdfImage_bot.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(pdfImage_bot);

            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No PDF viewer available", Toast.LENGTH_SHORT).show();
            }

            if (pdfFile.exists()) {
                Toast.makeText(this, "PDF Created and Saved Successfully", Toast.LENGTH_SHORT).show();
                Log.d("PDF","note : "+pdfFile);
            } else {
                Toast.makeText(this, "Failed to Save PDF", Toast.LENGTH_SHORT).show();
                Log.d("PDF","note : "+pdfFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Cell createCell(String label, String value) {
        Cell cell = new Cell();
        cell.add(new Paragraph(label));
        cell.add(new Paragraph(value));
        return cell;
    }


}
