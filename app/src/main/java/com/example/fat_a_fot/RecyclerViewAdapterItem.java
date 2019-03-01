package com.example.fat_a_fot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

public class RecyclerViewAdapterItem extends RecyclerView.Adapter<RecyclerViewAdapterItem.MyViewHolder>{
    private JSONArray myValues;
    private Context context;
    public RecyclerViewAdapterItem (JSONArray myValues , Context context){
        this.myValues= myValues;
        this.context = context;
    }
    @Override
    public RecyclerViewAdapterItem.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcard, parent, false);
        return new RecyclerViewAdapterItem.MyViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdapterItem.MyViewHolder holder, int position) {
        try {

            Picasso.with(context).load(myValues.getJSONObject(position).getString("image").toString()).into(holder.image);
            holder.name.setText(myValues.getJSONObject(position).getString("name"));
            holder.id.setText(myValues.getJSONObject(position).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return myValues.length();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name,id;
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            id = (TextView)itemView.findViewById(R.id.id);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Fragment newFragment = new ItemDetails();
                        Bundle bundle = new Bundle();
                        bundle.putString("item_id",id.getText().toString());
                        newFragment.setArguments(bundle);
                        AppCompatActivity activity1 = (AppCompatActivity) v.getContext();
                        activity1.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, newFragment).addToBackStack(null).commit();
                    }
                }
            });
        }
    }
}
