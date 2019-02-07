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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        img=(ImageView) findViewById(R.id.splashImage);
        txt=(TextView)findViewById(R.id.txtview);
        Animation myanim=AnimationUtils.loadAnimation(this, R.anim.animation);
        img.startAnimation(myanim);
        txt.startAnimation(myanim);
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(Splashscreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);

        }
    }
