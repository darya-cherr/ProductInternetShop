package com.example.productinternetshop.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productinternetshop.Parser.ParseItem;
import com.example.productinternetshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private Context context;
    private List<ParseItem> favList;
    private FavDB favDB;

    public FavAdapter(Context context, List<ParseItem> favList){
        this.context = context;
        this.favList = favList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent,false);
        favDB = new FavDB(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseItem parseItem = favList.get(position);
        holder.brandTxt.setText(parseItem.getBrand());
        holder.nameTxt.setText(parseItem.getName());
        holder.priceTxt.setText(parseItem.getPrice());

        holder.favButton.setBackgroundResource(R.drawable.ic_heart_svgrepo_com);

        Picasso.get().load(parseItem.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
                    final ParseItem parseItem = favList.get(position);
                    favDB.remove_fav(parseItem.getKey_id());
                    removeItem(position);
                }
            });

        }

        private void removeItem(int position){
            favList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favList.size());

        }
    }
}
