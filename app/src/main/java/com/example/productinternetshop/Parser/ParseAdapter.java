package com.example.productinternetshop.Parser;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productinternetshop.ItemDetailActivity;
import com.example.productinternetshop.R;
import com.example.productinternetshop.Utilities.FavDB;
import com.squareup.picasso.Picasso;

import java.sql.SQLData;
import java.util.ArrayList;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    public ArrayList<ParseItem> parseItems;
    private Context context;
    private FavDB favDB;

    public ParseAdapter(ArrayList<ParseItem> parseItems, Context context){
        this.parseItems = parseItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        favDB = new FavDB(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if(firstStart){
            createTableOnFirstStart();
        }
        return new ViewHolder(view);
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty(parseItems.size());
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {
        ParseItem parseItem = parseItems.get(position);
        holder.brandTxt.setText(parseItem.getBrand());
        holder.nameTxt.setText(parseItem.getName());
        holder.priceTxt.setText(parseItem.getPrice());
        Picasso.get().load(parseItem.getImgUrl()).into(holder.imageView);

        Cursor cursor = favDB.read_all_data(parseItem.getKey_id());
                SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                Log.d("MyLog", cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID)));
                parseItem.setFavStatus(item_fav_status);
                if ( item_fav_status.equals("1")) {
                    holder.favButton.setBackgroundResource(R.drawable.ic_heart_svgrepo_com);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public void clearItemsList () {
        parseItems.clear();
        notifyDataSetChanged();
    }

    public String getMaxPrice(){
        int maxPrice = 0;
        for(ParseItem parseItem : parseItems){
            if(maxPrice < Integer.parseInt(parseItem.getPrice())){
                maxPrice = Integer.parseInt(parseItem.getPrice());
            }
        }
        return Integer.toString(maxPrice);
    }

    public void setFilter(ArrayList<ParseItem> newList){
        parseItems = new ArrayList<>();
        parseItems.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        ImageButton favButton;
        TextView brandTxt, nameTxt, priceTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
            brandTxt =  itemView.findViewById(R.id.item_brand);
            nameTxt = itemView.findViewById(R.id.item_name);
            priceTxt = itemView.findViewById(R.id.item_price);
            favButton = itemView.findViewById(R.id.fav_heart);

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ParseItem parseItem = parseItems.get(position);

                    if(parseItem.getFavStatus().equals("0")){
                        parseItem.setFavStatus("1");
                        favDB.insertIntoTheDatabase(parseItem.getBrand(),parseItem.getName(),parseItem.getPrice(),parseItem.getImgUrl(), parseItem.getKey_id(), parseItem.getFavStatus());
                        favButton.setBackgroundResource(R.drawable.ic_heart_svgrepo_com);

                    }else{
                        parseItem.setFavStatus("0");
                        favDB.remove_fav(parseItem.getKey_id());
                        favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("brand", parseItem.getBrand());
            intent.putExtra("name", parseItem.getName());
            intent.putExtra("price",parseItem.getPrice());
            intent.putExtra("image",parseItem.getImgUrl());
            intent.putExtra("detailUrl",parseItem.getDetailUrl());
            context.startActivity(intent);
        }
    }
}
