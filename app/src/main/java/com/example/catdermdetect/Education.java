package com.example.catdermdetect;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.Html;

public class Education extends AppCompatActivity {

    View layout_item_education;
    TextView detail1, detail2, detail3, detail4, detail5, detail6, detail7;
    LinearLayout layout1, layout2, layout3,layout4, layout5, layout6,  layout7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        layout_item_education = findViewById(R.id.layout_item_education);
        detail1 = layout_item_education.findViewById(R.id.detail1);
        layout1 = layout_item_education.findViewById(R.id.layout1);
        layout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail2 = layout_item_education.findViewById(R.id.detail2);
        layout2 = layout_item_education.findViewById(R.id.layout2);
        layout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail3 = layout_item_education.findViewById(R.id.detail3);
        layout3 = layout_item_education.findViewById(R.id.layout3);
        layout3.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail4 = layout_item_education.findViewById(R.id.detail4);
        layout4 = layout_item_education.findViewById(R.id.layout4);
        layout4.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail5 = layout_item_education.findViewById(R.id.detail5);
        layout5 = layout_item_education.findViewById(R.id.layout5);
        layout5.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail6 = layout_item_education.findViewById(R.id.detail6);
        layout6 = layout_item_education.findViewById(R.id.layout6);
        layout6.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        detail7 = layout_item_education.findViewById(R.id.detail7);
        layout7 = layout_item_education.findViewById(R.id.layout7);
        layout7.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

    }

    public void expand1(View view){
        int a = (detail1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout1, new AutoTransition());
        detail1.setVisibility(a);

    }

    public void expand2(View view){
        int b = (detail2.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout2, new AutoTransition());
        detail2.setVisibility(b);
    }

    public void expand3(View view){
        int c = (detail3.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout3, new AutoTransition());
        detail3.setVisibility(c);
    }

    public void expand4(View view){
        int d = (detail4.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout4, new AutoTransition());
        detail4.setVisibility(d);
    }

    public void expand5(View view){
        int e = (detail5.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout5, new AutoTransition());
        detail5.setVisibility(e);
    }

    public void expand6(View view){
        int f = (detail6.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout6, new AutoTransition());
        detail6.setVisibility(f);
    }

    public void expand7(View view){
        int g = (detail7.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(layout7, new AutoTransition());
        detail7.setVisibility(g);
    }


}