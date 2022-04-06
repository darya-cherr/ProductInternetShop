package com.example.productinternetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productinternetshop.Parser.ParseItem;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView brandText, nameText, priceText, descriptionText;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        imageView = findViewById(R.id.item_image_view);
        brandText = findViewById(R.id.item_brand);
        nameText = findViewById(R.id.item_name);
        priceText = findViewById(R.id.item_price);
        descriptionText = findViewById(R.id.item_description);

        ImageButton backButton = findViewById(R.id.detail_back_btn);
        brandText.setText(getIntent().getStringExtra("brand"));
        nameText.setText(getIntent().getStringExtra("name"));
        priceText.setText(getIntent().getStringExtra("price"));
        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);

        Content content = new Content();
        content.execute();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class Content extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            descriptionText.setText(description);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String detailUrl = getIntent().getStringExtra("detailUrl");
                Document document = Jsoup.connect(detailUrl).get();

                Elements data = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--custom_tab_s panel entry-content wc-tab");

                description = data.select("p")
                        .text();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}