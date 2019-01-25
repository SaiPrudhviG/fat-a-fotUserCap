package com.example.fat_a_fot;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class ShopkeeperCardRecyclerViewAdapter  extends RecyclerView.Adapter<ShopkeeperCardRecyclerViewAdapter.MyViewHolder>{
    private JSONArray myValues;
    private Context context;
    CardView cardView;

    public ShopkeeperCardRecyclerViewAdapter (JSONArray myValues,Context context){
            this.myValues= myValues;
            this.context=context;
            }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopkeeper_card, parent, false);
            return new MyViewHolder(listItem);
            }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            try {
            Log.e("test ",myValues.getJSONObject(position).getString("firstName"));
//            holder.name.setText(myValues.getJSONObject(position).getString("firstName"));
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
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
    //        image = (ImageView)itemView.findViewById(R.id.image);
    //        name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
