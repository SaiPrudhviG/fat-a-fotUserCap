package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Order_Placed_Details extends Fragment {
    private ProgressDialog pDialog;
    Button place_order;
    EditText name,address,city,state,mobile;
    TextView total;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_place_details, container, false);
        place_order =(Button) v.findViewById(R.id.order);
        name = (EditText)v.findViewById(R.id.name);
        address = (EditText)v.findViewById(R.id.address);
        city = (EditText)v.findViewById(R.id.city);
        state = (EditText)v.findViewById(R.id.state);
        mobile = (EditText)v.findViewById(R.id.mobile);
        total = (TextView)v.findViewById(R.id.total);
        return v;
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Details");

        this.view = view;
        if (Detectconnection.checkInternetConnection(view.getContext())) {
            if (Common.getSavedUserData(view.getContext(), "mobile").equalsIgnoreCase("")) {
                Intent intent = new Intent(view.getContext(), Signup.class);
                startActivity(intent);
                Toast.makeText(view.getContext(), " Please Login Again.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(view.getContext(), "Check Internet Connection.", Toast.LENGTH_LONG).show();
            Intent noconnection = new Intent(view.getContext(), NoInternetConnectionActivity.class);
            startActivity(noconnection);
        }
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyOrder(view);
            }
        });
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void updateMyOrder(final View view) {
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "Cart Card";
        pDialog.setMessage("Wait Some...");
        showDialog();
        Toast.makeText(getActivity(), "In Order Placed", Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        Fragment newFragment = new ThankU();
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
                params.put("mobile", Common.getSavedUserData(view.getContext(), "mobile"));
                params.put("name", name.getText().toString());
                params.put("address", address.getText().toString());
                params.put("city", city.getText().toString());
                params.put("state", state.getText().toString());
                params.put("order_mobile", mobile.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}