package com.example.catdermdetect;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class CustomItemDetailObat {
    private Activity activity;
    private AlertDialog dialog;

    CustomItemDetailObat(Activity myactivity){
        activity = myactivity;
    }

    void open(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.item_data_obat, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void close(){
        dialog.dismiss();
    }


}
