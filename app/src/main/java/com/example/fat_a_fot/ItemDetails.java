package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ItemDetails extends Fragment {
    private ProgressDialog pDialog;
    View view;
    String item_id,shop_id;
    Toolbar toolbar_name;
    ImageView image_large;
    TextView full_total,full_price,small_price,half_price,small_total,half_total,item_quant_small,item_quant_half,item_quant_full;
    Button full_sub,small_add,half_sub,half_add,full_add,small_sub,addCart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_id = getArguments().getString("item_id");
        this.shop_id = getArguments().getString("shop_id");
        return inflater.inflate(R.layout.activity_item_details, container, false);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Item");
        this.view = view;
        items(this.view);
        toolbar_name = (Toolbar) getActivity().findViewById(R.id.toolbar_name);
        image_large = view.findViewById(R.id.image_large);
        full_add = view.findViewById(R.id.full_add);
        full_sub = view.findViewById(R.id.full_minus);
        full_total = view.findViewById(R.id.full_total);

        small_add = view.findViewById(R.id.small_add);
        small_sub = view.findViewById(R.id.small_minus);
        small_total = view.findViewById(R.id.small_total);

        half_add = view.findViewById(R.id.half_add);
        half_sub = view.findViewById(R.id.half_minus);
        half_total = view.findViewById(R.id.half_total);

        full_price = view.findViewById(R.id.full_price);
        small_price = view.findViewById(R.id.small_price);
        half_price = view.findViewById(R.id.half_price);

        item_quant_small = view.findViewById(R.id.item_quant_small);
        item_quant_half = view.findViewById(R.id.half_item_quant);
        item_quant_full = view.findViewById(R.id.full_item_quant);

        addCart = view.findViewById(R.id.addCart);

        full_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_full.getText().toString().trim());
                i++;
                Integer total = Integer.parseInt(full_price.getText().toString()) * i;
                full_total.setText("Rs."+total+" ");
                item_quant_full.setText(i+" ");

            }
        });
        full_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_full.getText().toString().trim());
                i--;
                if(i>=0){
                    Integer total = Integer.parseInt(full_price.getText().toString()) * i;
                    full_total.setText("Rs."+total+" ");
                    item_quant_full.setText(i+" ");

                }
            }
        });

        small_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_small.getText().toString().trim());
                i++;
                Integer total = Integer.parseInt(small_price.getText().toString()) * i;
                small_total.setText("Rs."+total+" ");
                item_quant_small.setText(i+" ");

            }
        });
        small_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_small.getText().toString().trim());
                i--;
                if(i>=0){
                    Integer total = Integer.parseInt(small_price.getText().toString()) * i;
                    small_total.setText("Rs."+total+" ");
                    item_quant_small.setText(i+" ");

                }
            }
        });

        half_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_half.getText().toString().trim());
                i++;
                Integer total = Integer.parseInt(half_price.getText().toString()) * i;
                half_total.setText("Rs."+total+" ");
                item_quant_half.setText(i+" ");

            }
        });
        half_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(item_quant_half.getText().toString().trim());
                i--;
                if(i>=0){
                    Integer total = Integer.parseInt(half_price.getText().toString()) * i;
                    half_total.setText("Rs."+total+" ");
                    item_quant_half.setText(i+" ");

                }
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(view);
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
    public void items(final View view) {
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "Items Card";
        pDialog.setMessage("Fetching Items Details...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ITEM_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        JSONObject item = jObj.getJSONObject("item");
                        toolbar_name.setTitle(item.getString("name"));
                        Picasso.with(view.getContext()).load(item.getString("image")).into(image_large);
                        full_price.setText(item.getString("full"));
                        small_price.setText(item.getString("small"));
                        half_price.setText(item.getString("half"));
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
                params.put("id", item_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void addCart(final View view){
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        String tag_string_req = "Items Card";
        pDialog.setMessage("Adding Item Into Cart...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_ADD_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        Fragment newFragment = new UserCartFragment();
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
                Log.d("data",error.toString());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile",Common.getSavedUserData(getContext() ,"mobile"));
                params.put("shopkeeper",shop_id);
                params.put("item",item_id);
                params.put("full",item_quant_full.getText().toString());
                params.put("small",item_quant_small.getText().toString());
                params.put("half",item_quant_half.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}