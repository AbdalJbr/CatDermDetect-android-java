package com.example.catdermdetect.HelperClasses.HomeAdapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.catdermdetect.ListObatVit;
import com.example.catdermdetect.ListPenyakit;
import com.example.catdermdetect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ObatVitAdapter extends RecyclerView.Adapter<ObatVitAdapter.ObatViewHolder> {
    private List<ListObatVit> obatVitList;
    private Context context;
    private FirebaseStorage storage;

    public ObatVitAdapter(Context context, List<ListObatVit> obatVitList) {
        this.context = context;
        this.obatVitList = obatVitList;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ObatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu_obat, parent, false);
        return new ObatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObatViewHolder holder, int position) {
        ListObatVit obat = obatVitList.get(position);
        holder.txtNamaObat.setText(obat.getNama_obat());

        String nm_img = obat.getKd_obat().toString().trim();
        String gsUrl =  "gs://catdermdetect.appspot.com/imgObatVit/"+nm_img+".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(gsUrl);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();

                Glide.with(holder.itemView.getContext())
                        .load(downloadUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.skinnycats__1_)
                                .error(R.drawable.ic_error))
                        .into(holder.imgObat);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle error
                Log.e("ObatVitAdapter", "Failed to get download URL", e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return obatVitList.size();
    }

    public class ObatViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamaObat;
        ImageView imgObat;

        public ObatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaObat = itemView.findViewById(R.id.txtObatVit);
            imgObat = itemView.findViewById(R.id.imgObat);

            imgObat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ListObatVit obat = obatVitList.get(position);
                        showCustomItemDataDialog(context, obat);
                    }
                }
            });
        }
    }

    private void showCustomItemDataDialog(Context context, ListObatVit obat) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_data_obat);

        ImageView imgDetail = dialog.findViewById(R.id.imgDetail);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDetail = dialog.findViewById(R.id.txtDetail);

        txtTitle.setText(obat.getNama_obat());
        txtDetail.setText(obat.getFungsi());

        String nm_img = obat.getKd_obat().toString().trim();
        String gsUrl = "gs://catdermdetect.appspot.com/imgObatVit/" + nm_img + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(gsUrl);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();
                Glide.with(context)
                        .load(downloadUrl)
                        .into(imgDetail);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle error
                Log.e("ObatVitAdapter", "Failed to get download URL", e);
            }
        });

        dialog.show();
    }
}
