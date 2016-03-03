package yithian.cosacompro.db.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.Seller;

public class SellerHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "seller";
    // Table fields
    private static final String COLUMN_SELLER_ID = "seller_id";
    private static final String COLUMN_SELLER_NAME = "seller_name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_SELLER_ID + " INTEGER PRIMARY KEY NOT NULL DEFAULT (0), " +
            COLUMN_SELLER_NAME + " TEXT NOT NULL, " +
            COLUMN_ADDRESS + " TEXT, " +
            COLUMN_CITY + " TEXT " +
            ");";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public SellerHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //ADD a new row to the database
    public void addSeller(Seller seller) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SELLER_NAME, seller.getSeller_name());
        if (seller.getAddress() != null) values.put(COLUMN_ADDRESS, seller.getAddress());
        if (seller.getCity() != null) values.put(COLUMN_CITY, seller.getCity());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // DELETE a seller from the table
    public void deleteSeller(Seller seller) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_SELLER_ID + "=" + seller.getSeller_id() + ";");
        db.close();
    }

    public void deleteSellerbyID(int sellerID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_SELLER_ID + "=" + sellerID + ";");
        db.close();
    }

    public void deleteSellerbyName(String sellerName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_SELLER_NAME + "=\"" + sellerName + "\";");
        db.close();
    }

    // DELETE all sellers from the table
    public void deleteAllSellers() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // GET all Sellers
    public ArrayList<Seller> getSellers() {
        ArrayList<Seller> resSellerList = new ArrayList<Seller>();
        SQLiteDatabase db = getWritableDatabase();
        Seller tempSeller;
        int temp_seller_id;
        String temp_seller_name, temp_address, temp_city;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_seller_id = c.getInt(c.getColumnIndex("seller_id"));
            temp_seller_name = c.getString(c.getColumnIndex("seller_name"));
            temp_address = c.getString(c.getColumnIndex("address"));
            temp_city = c.getString(c.getColumnIndex("city"));
            tempSeller = new Seller(temp_seller_id, temp_seller_name, temp_address, temp_city);
            resSellerList.add(tempSeller);
            c.moveToNext();
        }
        return resSellerList;
    }

    //Print out the sellers as ArrayList
    public ArrayList<String> sellersToArrayList() {
        String temp;
        ArrayList<String> resList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp = "ID #" + c.getInt(c.getColumnIndex("seller_id")) + ": ";
            if (c.getString(c.getColumnIndex("seller_name")) != null) {
                temp += c.getString(c.getColumnIndex("seller_name")) + " (";
                temp += c.getString(c.getColumnIndex("address")) + ", ";
                temp += c.getString(c.getColumnIndex("city")) + ")";
            }
            resList.add(temp);
            c.moveToNext();
        }
        db.close();
        return resList;
    }
}
