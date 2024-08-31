package com.example.catdermdetect;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CustomLoading {

    private Activity activity;
    private AlertDialog dialog;

    CustomLoading(Activity myactivity){
        activity = myactivity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void endLoading(){
        dialog.dismiss();

    }


}
