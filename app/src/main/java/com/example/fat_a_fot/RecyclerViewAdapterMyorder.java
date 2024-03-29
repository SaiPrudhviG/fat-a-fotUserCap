package com.example.fat_a_fot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class RecyclerViewAdapterMyorder extends RecyclerView.Adapter<RecyclerViewAdapterMyorder.MyViewHolder>{
    private Context context;    private JSONArray myValues;

    public RecyclerViewAdapterMyorder (JSONArray myValues , Context context){
        this.myValues= myValues;
        this.context = context;

    }

    @Override
    public RecyclerViewAdapterMyorder.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.myordercard, parent, false);
        return new RecyclerViewAdapterMyorder.MyViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdapterMyorder.MyViewHolder holder, int position) {
        try {
            Picasso.with(context).load(myValues.getJSONObject(position).getString("image").toString()).into(holder.image);
            holder.itemname.setText(myValues.getJSONObject(position).getString("name"));
            holder.qantity.setText("("+myValues.getJSONObject(position).getString("varient_quantity")+")");
            Integer price = Integer.valueOf(myValues.getJSONObject(position).getString("price")) * Integer.parseInt(myValues.getJSONObject(position).getString("varient_quantity"));
            holder.price.setText("Rs. "+ price);
            String status = myValues.getJSONObject(position).getString("status");
            if(status.matches("SA1")){
                holder.cancel.setText("Cancel");
                holder.status.setText("Your Order Is Preparing.");
            }else if(status.matches("SCD1")){
                holder.status.setText("Waiting for Delivery Boy!");
            }else if(status.matches("DB1")){
                holder.status.setText("On the way!");
            }else if(status.matches("DBD1")){
                holder.status.setText("Delivered.");
            }
            status = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myValues.length();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemname,qantity,price,status,cancel;
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            itemname = (TextView)itemView.findViewById(R.id.itemname);
            price = (TextView)itemView.findViewById(R.id.price);
            qantity = (TextView)itemView.findViewById(R.id.quantnum);
            status = (TextView)itemView.findViewById(R.id.status);
            cancel=(TextView)itemView.findViewById(R.id.cancel);
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