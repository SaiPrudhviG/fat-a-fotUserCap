package com.example.fat_a_fot;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapterCart extends RecyclerView.Adapter<RecyclerViewAdapterCart.MyViewHolder>{
    private JSONArray myValues;
    private Context context;
    private ProgressDialog pDialog;
    public RecyclerViewAdapterCart (JSONArray myValues , Context context){
        this.myValues= myValues;
        this.context = context;
    }
    @Override
    public RecyclerViewAdapterCart.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcartcard, parent, false);
        return new RecyclerViewAdapterCart.MyViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdapterCart.MyViewHolder holder, int position) {
        try {
            String rs = myValues.getJSONObject(position).getString("price");
            String quan = myValues.getJSONObject(position).getString("quantity");
            Integer total = Integer.valueOf(rs) * Integer.valueOf(quan);
            Picasso.with(context).load(myValues.getJSONObject(position).getString("image").toString()).into(holder.image);
            holder.name.setText(myValues.getJSONObject(position).getString("name"));
            holder.item_id.setText(myValues.getJSONObject(position).getString("item_id"));
            holder.cart_id.setText(myValues.getJSONObject(position).getString("cart_id"));
            holder.price.setText("  Rs."+myValues.getJSONObject(position).getString("price"));
            holder.quantity.setText(myValues.getJSONObject(position).getString("quantity"));
            holder.total.setText("Rs."+total.toString());
            holder.variant.setText(myValues.getJSONObject(position).getString("variant"));
            holder.shop_name.setText(myValues.getJSONObject(position).getString("shop_name"));
            holder.shop_id.setText(myValues.getJSONObject(position).getString("shop_id"));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCart(context,holder.cart_id.getText().toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void deleteCart(final Context context, final String cart_id) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        String tag_string_req = "Items Card";
        pDialog.setMessage("Deleting Cart Item Details...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_USER_REMOVE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        Fragment newFragment = new UserCartFragment();
                        AppCompatActivity activity1 = (AppCompatActivity) context;
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
                params.put("cart_id", cart_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public int getItemCount() {
        return myValues.length();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name,price,quantity,total,variant,item_id,cart_id,shop_name,shop_id;
        private ImageView image;
        private Button delete,place_order;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            item_id = (TextView)itemView.findViewById(R.id.item_id);
            cart_id = (TextView)itemView.findViewById(R.id.cart_id);
            price = (TextView)itemView.findViewById(R.id.price);
            quantity = (TextView)itemView.findViewById(R.id.quantity);
            total = (TextView)itemView.findViewById(R.id.total);
            delete = (Button)itemView.findViewById(R.id.delete);
            variant = (TextView)itemView.findViewById(R.id.variant);
            shop_name = (TextView)itemView.findViewById(R.id.shop_name);
            shop_id = (TextView)itemView.findViewById(R.id.shop_id);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                }
                }
            });
        }
    }
}