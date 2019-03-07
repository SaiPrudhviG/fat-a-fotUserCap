package com.example.fat_a_fot;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private Context context;
    public void onTokenReftesh(Context context){
        this.context = context;
    }
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        final String token = FirebaseInstanceId.getInstance().getToken();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.FCM_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        Log.d("token",token);
                    } else {
                        String errorMsg = jObj.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", token);
                params.put("module", "user");
                params.put("id", Common.getSavedUserData(context,"userId"));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "FCM TOKEN");
    }
}

