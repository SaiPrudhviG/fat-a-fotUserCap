package com.example.fat_a_fot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterOrderPlaced extends AppCompatActivity {
Button trackorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order_placed);
        trackorder=findViewById(R.id.trackorder);
        trackorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent track =new Intent(AfterOrderPlaced.this,MyOrderFragment.class);
                startActivity(track);
            }
        });
    }
}
