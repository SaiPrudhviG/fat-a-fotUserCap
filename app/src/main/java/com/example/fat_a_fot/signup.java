package com.example.fat_a_fot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class signup extends AppCompatActivity {
EditText mobilenumber;
TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mobilenumber=(EditText)findViewById(R.id.mobilenumber);
        mobilenumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        submit=(TextView)findViewById(R.id.txtSignup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new1=new Intent(signup.this,otp_activity.class);
                startActivity(new1);
            }
        });
    }
}
