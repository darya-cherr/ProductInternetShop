package com.example.productinternetshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.productinternetshop.Parser.ParseAdapter;
import com.example.productinternetshop.Parser.ParseItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import me.bendik.simplerangeview.SimpleRangeView;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    int minSelectedPrice = 0, maxSelectedPrice;

    private RecyclerView recyclerView;
    private SwitchCompat switchCompat;
    private ParseAdapter parseAdapter;

    private AppCompatButton searchButton;
    private LinearLayout searchLayout;
    private SearchView searchView;

    private SimpleRangeView rangeBar;
    private TextView rangeText;
    private LinearLayout priceFilter;

    private ImageButton filterButton;

    private AppCompatButton aboutButton;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();

    boolean searchIsOpen;
    boolean priceFilterIsOpen;



    public HomeFragment() {

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchIsOpen) {
                    searchLayout.setVisibility(View.GONE);
                    searchIsOpen = false;
                }
                else {
                    searchLayout.setVisibility(View.VISIBLE);
                    searchIsOpen = true;
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ParseItem> newList = new ArrayList<>();
                for (ParseItem parseItem : parseItems){
                    String brand = parseItem.getBrand().toLowerCase();
                    String name = parseItem.getName().toLowerCase();
                    if(brand.contains(newText) || name.contains(newText)){
                        newList.add(parseItem);
                    }
                }
                parseAdapter.setFilter(newList);
                return true;
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceFilterIsOpen) {
                    priceFilter.setVisibility(View.GONE);
                     priceFilterIsOpen = false;
                }
                else {
                    String maxPrice = parseAdapter.getMaxPrice();
                    maxSelectedPrice = Integer.parseInt(maxPrice);
                    rangeText.setText("0-"+maxPrice);
                    rangeBar.setCount(Integer.parseInt(maxPrice));
                    priceFilter.setVisibility(View.VISIBLE);
                    priceFilterIsOpen = true;
                }
            }
        });

        rangeBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i, int i1) {
                rangeText.setText(String.valueOf(i) + "-" + String.valueOf(i1));
                minSelectedPrice = i;
                maxSelectedPrice = i1;
                ArrayList<ParseItem> newList = new ArrayList<>();
                for (ParseItem parseItem : parseItems){
                    int price = Integer.parseInt(parseItem.getPrice());
                    if(price >= minSelectedPrice && price <= maxSelectedPrice){
                        newList.add(parseItem);
                    }
                }
                parseAdapter.setFilter(newList);
            }
        });
        return view;
    }


    private void viewInit(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        switchCompat = view.findViewById(R.id.switch_button);
        aboutButton = view.findViewById(R.id.button_about_dev);

        searchButton = view.findViewById(R.id.search_btn);
        searchLayout = view.findViewById(R.id.search);
        searchView = view.findViewById(R.id.search_view);

        searchIsOpen = false;
        priceFilterIsOpen = false;

        rangeBar = view.findViewById(R.id.range_bar);
        rangeText = view.findViewById(R.id.range_bar_text);
        priceFilter =  view.findViewById(R.id.price_filter);

        filterButton = view.findViewById(R.id.filter_button);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseAdapter = new ParseAdapter(parseItems, getContext());
        recyclerView.setAdapter(parseAdapter);


        Content content = new Content("https://candlesbox.com/catalog/aromaticheskie-svechi/", true);
        content.execute();

    }

    private void switchCompatClickListener(){
        if(switchCompat.isChecked()){
            parseAdapter.clearItemsList();
            Content content = new Content("https://candlesbox.com/catalog/diffuzory/", false);
            content.execute();

        }else{
            parseAdapter.clearItemsList();
            Content content = new Content("https://candlesbox.com/catalog/aromaticheskie-svechi/", true);
            content.execute();

        }
    }

    private void aboutButtonClickListener(){
        Intent intent = new Intent(getContext(), AboutDevActivity.class);
        startActivity(intent);
    }


    private class Content extends AsyncTask<Void, Void, ArrayList<ParseItem>> {

        String url = "";
        boolean isCandle;


        private Content(String url, boolean isCandle){
            this.url = url;
            this.isCandle = isCandle;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(ArrayList<ParseItem> unused) {
            super.onPostExecute(unused);
            parseAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected ArrayList<ParseItem> doInBackground(Void... voids) {
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
                    String name = data.select("div.sostav")
                            .eq(i).text();
                    String price = data.select("span.price")
                            .eq(i).text();
                    price = price.substring(0, price.lastIndexOf("р")-1);
                    String detailUrl = data.select("h5")
                            .select("a")
                            .eq(i).attr("href");
                    if(isCandle) {
                        parseItems.add(new ParseItem(imgUrl, brand, name, price, detailUrl, "0", i));
                    }else{
                        parseItems.add(new ParseItem(imgUrl, brand, name, price, detailUrl, "0", i+22));
                    }

                    k++;
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }



}