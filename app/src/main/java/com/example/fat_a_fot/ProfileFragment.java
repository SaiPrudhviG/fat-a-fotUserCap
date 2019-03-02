package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private ProgressDialog pDialog;
    View view;
    public RadioGroup radioGenderGroup;
    public RadioButton radioGenderButton , radioButton_male,radioButton_female;
    private EditText name,email,mobile;
    private Button update;
    public int selectedId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("User Profile");
        this.view = view;
        radioGenderGroup = (RadioGroup)view.findViewById(R.id.radioGender);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        update = view.findViewById(R.id.update);
        radioButton_female = view.findViewById(R.id.FEMALE);
        radioButton_male = view.findViewById(R.id.MALE);
        selectedId = radioGenderGroup.getCheckedRadioButtonId();
        radioGenderButton = (RadioButton)view.findViewById(selectedId);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = radioGenderGroup.getCheckedRadioButtonId();
                radioGenderButton = (RadioButton)view.findViewById(selectedId);
                updateUserProfile(v);
            }
        });
        userProfile(view);
    }
    public void userProfile(final View view){
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "User Profile Card";
        pDialog.setMessage("Fetching User Details...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        JSONObject user = jObj.getJSONObject("user");
                        name.setText(user.getString("name"));
                        email.setText(user.getString("email"));
                        if(user.getString("gender").matches("Male")){
                            radioButton_male.setChecked(true);
                        }else if(user.getString("gender").matches("Female")){
                            radioButton_female.setChecked(true);
                        }
                        mobile.setText(user.getString("mobile"));
                        hideDialog();
                    } else {
                        String errorMsg = jObj.getString("message");
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", Common.getSavedUserData(view.getContext(), "mobile"));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void updateUserProfile(final View view){
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "User Profile Card";
        pDialog.setMessage("Updating User Details...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        Fragment newFragment = new ProfileFragment();
                        AppCompatActivity activity1 = (AppCompatActivity) getContext();
                        activity1.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, newFragment).addToBackStack(null).commit();
                        hideDialog();
                    } else {
                        String errorMsg = jObj.getString("message");
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", Common.getSavedUserData(view.getContext(),"mobile"));
                params.put("email", email.getText().toString());
                params.put("name", name.getText().toString());
                params.put("gender",radioGenderButton.getText().toString());
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
