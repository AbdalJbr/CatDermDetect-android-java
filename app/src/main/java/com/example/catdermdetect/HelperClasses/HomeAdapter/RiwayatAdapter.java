package com.example.catdermdetect.HelperClasses.HomeAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catdermdetect.BuildConfig;
import com.example.catdermdetect.CustomLoading;
import com.example.catdermdetect.ListRiwayat;
import com.example.catdermdetect.R;
import com.example.catdermdetect.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {

    private List<ListRiwayat> riwayatList;
    private Context context;
    private FirebaseFirestore db;

    public RiwayatAdapter(List<ListRiwayat>riwayatList , Context context){
        this.riwayatList = riwayatList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RiwayatAdapter.RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lay_riwayat, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.RiwayatViewHolder holder, int position ) {
        ListRiwayat riwayat = riwayatList.get(position);
        holder.id.setText(riwayat.getId_diagnosa());
        holder.date.setText(riwayat.getDate());

        String id = riwayat.getId_diagnosa();
        String username = riwayat.getUsername();
        String email = riwayat.getEmail();
        String kd_penyakit = riwayat.getKd_penyakit();
        String kd_gejala = riwayat.getKd_gejala();
        String solusi = riwayat.getSolusi();
        String date = riwayat.getDate();


        holder.btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama_file = id + "_" + username;
                File pdfFile = new File(context.getExternalFilesDir(null), nama_file + ".pdf");

                Toast.makeText(context, "Wait ....", Toast.LENGTH_SHORT).show();
                try {
                    PdfWriter writer = new PdfWriter(pdfFile);
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cover_top);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    ImageData imageData = ImageDataFactory.create(byteArray);
                    Image pdfImage = new Image(imageData);
                    pdfImage.setHorizontalAlignment(HorizontalAlignment.CENTER);

                    document.add(pdfImage);

                    Table table = new Table(3);
                    table.setHorizontalAlignment(HorizontalAlignment.LEFT);

                    table.addCell(createCell("ID:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(id));

                    table.addCell(createCell("Username:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(username));

                    table.addCell(createCell("Email:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(email));

                    table.addCell(createCell("Penyakit:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(kd_penyakit));

                    table.addCell(createCell("Gejala:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(kd_gejala));

                    table.addCell(createCell("Solusi:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(solusi));

                    table.addCell(createCell("Tanggal:"));
                    table.addCell(createCell(":"));
                    table.addCell(createCell(date));

                    document.add(table);
                    document.flush();

                    Bitmap bitmap_bot = BitmapFactory.decodeResource(context.getResources(), R.drawable.cover_bot);
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
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No PDF viewer available", Toast.LENGTH_SHORT).show();
                    }

                    if (pdfFile.exists()) {
                        Toast.makeText(context, "PDF Created and Saved Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("PDF", "note : " + pdfFile);
                    } else {
                        Toast.makeText(context, "Failed to Save PDF", Toast.LENGTH_SHORT).show();
                        Log.d("PDF", "note : " + pdfFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Wait ....", Toast.LENGTH_SHORT).show();
                db.collection("diagnosa")
                        .whereEqualTo("id_diagnosa", id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String documentId = document.getId();
                                        db.collection("diagnosa")
                                                .document(documentId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("DeleteDoc", "Document successfully deleted!");
                                                        riwayatList.remove(holder.getAdapterPosition());
                                                        notifyItemRemoved(holder.getAdapterPosition());
                                                        notifyItemRangeChanged(holder.getAdapterPosition(), riwayatList.size());
                                                        Toast.makeText(context, "Riwayat berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("DeleteDoc", "Error deleting document", e);
                                                        Toast.makeText(context, "Gagal menghapus riwayat", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d("DeleteDocs", "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
        });

    }

    private static Cell createCell(String content) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content));
        return cell;
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public static class RiwayatViewHolder extends RecyclerView.ViewHolder {
        TextView id, date;
        Button btnPdf, btnDelete;

        public  RiwayatViewHolder(@NonNull View itemView){
            super(itemView);
            id = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date);
            btnPdf = itemView.findViewById(R.id.pdf);
            btnDelete = itemView.findViewById(R.id.delete);
        }


    }
}
