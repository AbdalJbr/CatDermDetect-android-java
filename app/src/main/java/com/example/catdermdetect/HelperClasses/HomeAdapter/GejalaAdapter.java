package com.example.catdermdetect.HelperClasses.HomeAdapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catdermdetect.CheckDiagnosa;
import com.example.catdermdetect.HasilDiagnosa;
import com.example.catdermdetect.ListGejala;
import com.example.catdermdetect.ListPenyakit;
import com.example.catdermdetect.R;

import java.util.ArrayList;
import java.util.List;

public class GejalaAdapter extends RecyclerView.Adapter<GejalaAdapter.GejalaViewHolder> {

    private List<ListGejala> gejalaList;

    public GejalaAdapter(List<ListGejala> gejalaList) {
        this.gejalaList = gejalaList;
    }

    @NonNull
    @Override
    public GejalaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_diagnosa, parent, false);
        return new GejalaAdapter.GejalaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GejalaViewHolder holder, int position) {
        ListGejala gejala = gejalaList.get(position);
        holder.cekGejala.setText(gejala.getNm_gejala());
        holder.txtKode.setText(gejala.getKd_gejala());

        holder.cekGejala.setOnCheckedChangeListener(null);

        holder.cekGejala.setChecked(gejala.isChecked());

        holder.cekGejala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gejala.setChecked(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gejalaList.size();
    }
    public static class GejalaViewHolder extends RecyclerView.ViewHolder {
        CheckBox cekGejala;
        TextView txtKode;

        public GejalaViewHolder(@NonNull View itemView) {
            super(itemView);
            cekGejala = itemView.findViewById(R.id.cekGejala);
            txtKode = itemView.findViewById(R.id.txtKode);

        }
    }

}
