package com.example.fat_a_fot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoInternetConnectionActivity extends Activity {
    Button back_from_internetact;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nointernetconnection);
        back_from_internetact=findViewById(R.id.backfrmint);
        back_from_internetact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(NoInternetConnectionActivity.this,MainActivity.class);
                startActivity(n);
            }
        });
    }
}
