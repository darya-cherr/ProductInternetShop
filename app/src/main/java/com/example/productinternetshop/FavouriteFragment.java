package com.example.productinternetshop;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productinternetshop.Parser.ParseItem;
import com.example.productinternetshop.Utilities.FavAdapter;
import com.example.productinternetshop.Utilities.FavDB;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private TextView textView;
    private FavDB favDB;
    private List<ParseItem> favList = new ArrayList<>();
    private FavAdapter favAdapter;

    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {

    }

    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        favDB = new FavDB(getActivity());
        recyclerView = view.findViewById(R.id.fav_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        textView = view.findViewById(R.id.emptyTxt);

        loadData();

        return view;
    }


    @SuppressLint("Range")
    private void loadData() {
        if(favList != null){
            favList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try{
            while(cursor.moveToNext()){
               String name = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_NAME));
               String brand = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_BRAND));
               int key_id = cursor.getInt(cursor.getColumnIndex(FavDB.KEY_ID));
               String imgUrl = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE));
               String price = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_PRICE));

               ParseItem parseItem = new ParseItem(imgUrl,brand,name,price,key_id);
               favList.add(parseItem);
            }
        }finally{
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        if(favList != null) textView.setVisibility(View.GONE);
        favAdapter = new FavAdapter(getActivity(),favList);

        recyclerView.setAdapter(favAdapter);

    }
}