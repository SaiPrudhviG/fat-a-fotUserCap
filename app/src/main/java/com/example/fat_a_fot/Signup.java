package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private TextView btnsignup;
    private EditText mobile;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLLiteHandler db;
    private AppController appController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if(!(Common.getSavedUserData(Signup.this, "temp_mobile")).isEmpty()){
            Intent i = new Intent(Signup.this,Otp_activity.class);
            startActivity(i);
        }
        mobile = (EditText)findViewById(R.id.mobilenumber);
        mobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        btnsignup = (TextView) findViewById(R.id.txtSignup);

        appController = AppController.getInstance();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        btnsignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String mobilenumber = mobile.getText().toString().trim();
                mobilenumber = mobilenumber.replaceAll(" ","");
                if (!mobilenumber.isEmpty() && mobilenumber.length() == 10) {
                    checkLogin(mobilenumber);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter Mobile Number!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    private void checkLogin(final String mobile_number) {
        String tag_string_req = "req_login";
        pDialog.setMessage("Generating OTP...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        session.setLogin(true);
                        JSONObject user = jObj.getJSONObject("user");
                        String mobile = user.getString("mobile");
                        Common.saveUserData(Signup.this, "temp_mobile", mobile);
                        hideDialog();
                        Intent new1 = new Intent(Signup.this, Otp_activity.class);
                        new1.putExtra("otp",user.getString("otp"));
                        startActivity(new1);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("message");
                        hideDialog();
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.i("here",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_mobile", mobile_number);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
