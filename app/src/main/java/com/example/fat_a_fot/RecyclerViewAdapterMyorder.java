package com.example.fat_a_fot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
    private JSONArray myValues;
    private Context context;
    public RecyclerViewAdapterMyorder (JSONArray myValues , Context context){
        this.myValues= myValues;
        this.context = context;
    }
    @Override
    public RecyclerViewAdapterMyorder.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardshpokeeper, parent, false);
        return new RecyclerViewAdapterMyorder.MyViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdapterMyorder.MyViewHolder holder, int position) {
        try {
            Picasso.with(context).load(myValues.getJSONObject(position).getString("image").toString()).into(holder.image);
            holder.name.setText(myValues.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return myValues.length();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private LinearLayout deliveryboy_details;
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
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