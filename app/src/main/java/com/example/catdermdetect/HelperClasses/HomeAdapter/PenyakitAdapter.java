package com.example.catdermdetect.HelperClasses.HomeAdapter;

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
import com.example.catdermdetect.ObatVit;
import com.example.catdermdetect.R;
import com.example.catdermdetect.ListPenyakit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.PenyakitViewHolder> {
    private List<ListPenyakit> penyakitList;

    public PenyakitAdapter(List<ListPenyakit> penyakitList) {
        this.penyakitList = penyakitList;
    }

    @NonNull
    @Override
    public PenyakitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu, parent, false);
        return new PenyakitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyakitViewHolder holder, int position) {
        ListPenyakit penyakit = penyakitList.get(position);
        holder.title.setText(penyakit.getNm_penyakit());
        holder.detail.setText(penyakit.getDeskripsi());

        String kd_img = penyakit.getKd_penyakit().toString().trim();
        Log.d("Test Penyakit","Note"+kd_img);
        String gsUrl =  "gs://catdermdetect.appspot.com/imgPenyakit/"+kd_img+".jpg";
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
                        .into(holder.imgPenyakit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle error
                Log.e("PenyakitAdapter", "Failed to get download URL", e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public static class PenyakitViewHolder extends RecyclerView.ViewHolder {
        TextView title,detail;
        ImageView imgPenyakit;

        public PenyakitViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tittle);
            detail = itemView.findViewById(R.id.detail);
            imgPenyakit = itemView.findViewById(R.id.imgPenyakit);

        }
    }
}
