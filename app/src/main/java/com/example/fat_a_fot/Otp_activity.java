package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
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

public class Otp_activity extends AppCompatActivity {
    public EditText otp;
    private TextView submit_otp,resend_otp;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLLiteHandler db;
    private AppController appController;
    private MyFirebaseInstanceIdService token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_activity);
        otp = (EditText)findViewById(R.id.otp);
        otp.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        token = new MyFirebaseInstanceIdService();
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String value = extras.getString("otp");
            otp.setText(value);
        }
        resend_otp = findViewById(R.id.resendotp);
        submit_otp = findViewById(R.id.submit_otp);
        appController = AppController.getInstance();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpnumber = otp.getText().toString().trim();
                otpnumber = otpnumber.replaceAll(" ","");
                String mobile_number = Common.getSavedUserData(Otp_activity.this, "temp_mobile");
                if (!otpnumber.isEmpty() ) {
                    checkLogin(otpnumber ,mobile_number);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter OTP Number!", Toast.LENGTH_LONG).show();
                }
            }
        });
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_number = Common.getSavedUserData(Otp_activity.this, "temp_mobile");
                    resend_otp(mobile_number);
            }
        });
    }
    private void resend_otp(final String mobile_number) {
        String tag_string_req = "req_login";
        pDialog.setMessage("Resend OTP  ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        JSONObject user = jObj.getJSONObject("user");
                        otp.setText(user.getString("otp"));
                        hideDialog();
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
    private void checkLogin(final String otp_number,final String mobile_number) {
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_OTP_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        session.setLogin(true);
                        JSONObject user = jObj.getJSONObject("user");
                        if(! user.getString("name").isEmpty()){
                            String id = user.getString("id");
                            String name = user.getString("name");
                            String email = user.getString("email");
                            String gender = user.getString("gender");
                            String mobile = user.getString("mobile");
                            db.addUser(name, email, id, mobile);
                            Common.saveUserData(Otp_activity.this, "email", email);
                            Common.saveUserData(Otp_activity.this, "userId", id+"");
                            Common.saveUserData(Otp_activity.this, "name", name);
                            Common.saveUserData(Otp_activity.this, "gender", gender);
                            Common.saveUserData(Otp_activity.this, "mobile", mobile);
                        }else{
                            String mobile = user.getString("mobile");
                            db.addUser(mobile);
                            Common.saveUserData(Otp_activity.this, "mobile", mobile);
                        }
                        hideDialog();
                        token.onTokenRefresh();
                        token.onTokenReftesh(Otp_activity.this);
                        Intent new1 = new Intent(Otp_activity.this, MainActivity.class);
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
                params.put("user_otp", otp_number);
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
