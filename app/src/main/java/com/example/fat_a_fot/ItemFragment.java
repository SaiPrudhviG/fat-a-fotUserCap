package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Items");
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.view = view;
//        if (Detectconnection.checkInternetConnection(view.getContext())) {
//            if (Common.getSavedUserData(view.getContext(), "mobile").equalsIgnoreCase("")) {
//                Intent intent = new Intent(view.getContext(), Signup.class);
//                startActivity(intent);
//                Toast.makeText(view.getContext(), " Please Login Again.", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Toast.makeText(view.getContext(), "Check Internet Connection.", Toast.LENGTH_LONG).show();
//            Intent noconnection = new Intent(view.getContext(), NoInternetConnectionActivity.class);
//            startActivity(noconnection);
//        }
        items(this.view);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void items(final View view) {
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "Items Card";
        pDialog.setMessage("Fetching Items Details...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ITEM_CARD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        RecyclerViewAdapterItem adapter = new RecyclerViewAdapterItem(jObj.getJSONArray("item"), view.getContext());
                        RecyclerView myView = (RecyclerView) getView().findViewById(R.id.recycle_items);
                        myView.setHasFixedSize(true);
                        myView.setAdapter(adapter);
                        GridLayoutManager llm = new GridLayoutManager(view.getContext(),2);
                        myView.setLayoutManager(llm);
                        hideDialog();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        String errorMsg = jObj.getString("message");
                        hideDialog();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                swipeRefreshLayout.setRefreshing(false);
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
    @Override
    public void onRefresh() {
        items(view);
    }
}