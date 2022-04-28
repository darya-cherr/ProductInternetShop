package com.example.productinternetshop.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class FavDB extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "ProductDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_BRAND = "itemBrand";
    public static String ITEM_NAME = "itemName";
    public static String ITEM_PRICE = "itemPrice";
    public static String ITEM_IMAGE = "itemImage";
    public static String FAVORITE_STATUS = "fStatus";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + ITEM_BRAND + " TEXT," + ITEM_NAME + " TEXT," + ITEM_PRICE + " TEXT,"
            + ITEM_IMAGE + " TEXT," + FAVORITE_STATUS+" TEXT)";

    public FavDB(Context context) { super(context,DATABASE_NAME,null,DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertEmpty(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int x = 1; x < index+1; x++) {
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS, "0");

            db.insert(TABLE_NAME,null, cv);
        }
    }

    public void insertIntoTheDatabase(String item_brand, String item_name, String item_price, String item_image, int id, String fav_status) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_BRAND, item_brand);
        cv.put(ITEM_NAME, item_name);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(KEY_ID, id);
        cv.put(FAVORITE_STATUS, fav_status);
        db.insert(TABLE_NAME,null, cv);
        Log.d("MyLog", item_brand + ", favstatus - "+fav_status+" - . " + cv);
    }

    public Cursor read_all_data(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID+"="+id+"";
        return db.rawQuery(sql,null,null);
    }

    public void remove_fav(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET  "+ FAVORITE_STATUS+" ='0' WHERE "+KEY_ID+"="+id+"";
        db.execSQL(sql);
        Log.d("MyLog", String.valueOf(id));
    }

    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }

}
