package com.example.productinternetshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.productinternetshop.Parser.ParseAdapter;
import com.example.productinternetshop.Parser.ParseItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SwitchCompat switchCompat;
    private ParseAdapter parseAdapter;
    private AppCompatButton aboutButton;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewInit(view);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompatClickListener();
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutButtonClickListener();
            }
        });
        return view;
    }

    private void viewInit(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        switchCompat = view.findViewById(R.id.switch_button);
        aboutButton = view.findViewById(R.id.button_about_dev);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseAdapter = new ParseAdapter(parseItems, getContext());
        recyclerView.setAdapter(parseAdapter);

        if(!switchCompat.isChecked()){
            Content content = new Content("https://candlesbox.com/catalog/aromaticheskie-svechi/");
            content.execute();}
    }

    private void switchCompatClickListener(){
        if(switchCompat.isChecked()){
            parseAdapter.clearItemsList();
            Content content = new Content("https://candlesbox.com/catalog/diffuzory/");
            content.execute();
        }else{
            parseAdapter.clearItemsList();
            Content content = new Content("https://candlesbox.com/catalog/aromaticheskie-svechi/");
            content.execute();
        }
    }

    private void aboutButtonClickListener(){
        Intent intent = new Intent(getContext(), AboutDevActivity.class);
        startActivity(intent);
    }

    private class Content extends AsyncTask<Void, Void, Void>{

        String url = "";

        private Content(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            parseAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Document document = Jsoup.connect(url).get();

                Elements data = document.getElementsByClass("products row").select("li");

                int size = data.size();
                int k = 1;
                for (int i = 0; i < size; i++){
                    String imgUrl = data.select("div.product_image")
                            .select("a").select("img")
                            .eq(i+k).attr("src");

                    String brand = data.select("h5")
                            .select("a[href]")
                            .eq(i).text();
                    brand = brand.substring(brand.lastIndexOf(',')+2);
                    Log.d("MyLog", brand);
                    String name = data.select("div.sostav")
                            .eq(i).text();
                    String price = data.select("span.price")
                            .eq(i).text();
                    parseItems.add(new ParseItem(imgUrl, brand, name, price));
                    k++;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }



}