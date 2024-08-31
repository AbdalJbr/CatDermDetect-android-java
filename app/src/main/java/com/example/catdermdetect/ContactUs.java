package com.example.catdermdetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    View layout_contact;
    TextView phone,email,whatsapp;
    ImageView facebook, instagram, share;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        layout_contact = findViewById(R.id.layout_contact);
        phone = layout_contact.findViewById(R.id.phone);
        email = layout_contact.findViewById(R.id.email);
        whatsapp = layout_contact.findViewById(R.id.whatsapp);
        facebook = layout_contact.findViewById(R.id.facebook);
        instagram = layout_contact.findViewById(R.id.instagram);
        share = layout_contact.findViewById(R.id.share);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ContactUs.this, HomeScreen.class);
                startActivity(move);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = "6285691712762";

                Uri uri = Uri.parse("tel:" + phoneNum);
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(uri);
                startActivity(call);

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = "catdermdetect.cdd@gmail.com";
                String subject = "Aplikasi Cat Derm Detect";
                String body = "Hai Develop CDD\n\n" +
                        "Silahkan isi disini...." +
                        "\n\nBest Regards,\n";

                Uri uri = Uri.parse("mailto:" + emailAddress + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));

                Intent mail = new Intent(Intent.ACTION_SENDTO);
                mail.setData(uri);

                startActivity(Intent.createChooser(mail, "Pilih aplikasi email"));

            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "6285691712762";
                String message = "Hai Dev Cat Derm Detect,\n\nSilahkan isi......\n\nBest Regards,\n";
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message);
                Intent sendWa = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendWa);

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookProfileUrl = "https://www.facebook.com/abdal.jabar.12";
                Uri uri = Uri.parse(facebookProfileUrl);
                Intent moveFb = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(moveFb);

            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instagramProfileUrl = "https://www.instagram.com/abdaljbr_";
                Uri uri = Uri.parse(instagramProfileUrl);
                Intent moveIg = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(moveIg);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appUrl = "https://drive.google.com/drive/folders/1rUeijGZ0Utexqt0nOQ_95oEUvj5Y7Rzk?usp=sharing";
               // https://play.google.com/store/apps/details?id=your.package.name
                String shareMessage = "Coba aplikasi kami!";

                Intent shareApp = new Intent(Intent.ACTION_SEND);
                shareApp.setType("text/plain");
                shareApp.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + appUrl);

                startActivity(Intent.createChooser(shareApp, "Bagikan aplikasi melalui"));

            }
        });

    }

}