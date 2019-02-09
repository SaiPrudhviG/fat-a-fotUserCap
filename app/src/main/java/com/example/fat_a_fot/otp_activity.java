package com.example.fat_a_fot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.EditText;

public class otp_activity extends AppCompatActivity {
EditText otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_activity);
        otp=(EditText)findViewById(R.id.otp);
        otp.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
}
