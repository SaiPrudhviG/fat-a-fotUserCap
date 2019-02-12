package com.example.fat_a_fot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {
    Handler handler;
    ImageView img;
    TextView txt;
    private int SPLASH_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        img=(ImageView) findViewById(R.id.splashImage);
        txt=(TextView)findViewById(R.id.txtview);
        Animation myanim=AnimationUtils.loadAnimation(this, R.anim.animation);
        img.startAnimation(myanim);
        txt.startAnimation(myanim);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Common.getSavedUserData(Splashscreen.this, "mobile").equalsIgnoreCase("")) {
                        Intent intent = new Intent(Splashscreen.this, Signup.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            },SPLASH_TIME);

        }
    }
