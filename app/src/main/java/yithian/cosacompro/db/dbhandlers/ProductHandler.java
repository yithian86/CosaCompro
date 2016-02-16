package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.Product;

public class ProductHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "product";
    // Table fields
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_BARCODE = "barCode";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_DESCRIPTION = "weight_volume";
    private static final String COLUMN_CATEGORY = "cat_name_fk";
    // Foreign keys
    private static final String CATEGORY_TABLE = "category";
    private static final String COLUMN_CAT_NAME = "cat_name";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0), " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_BARCODE + " TEXT, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            COLUMN_BRAND + " TEXT NOT NULL, " +
            COLUMN_CATEGORY + " TEXT, " +
            " FOREIGN KEY (" + COLUMN_CATEGORY + ") REFERENCES " + CATEGORY_TABLE + " (" + COLUMN_CAT_NAME + ") " +
            ");";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";
    private Product productTable;

    public ProductHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BARCODE, product.getBarcode());
        values.put(COLUMN_PRODUCT_NAME, product.getProduct_name());
        values.put(COLUMN_BRAND, product.getBrand());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CATEGORY, product.getCategory());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // DELETE all products from the DB
    public void deleteAllProducts() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //DELETE a product by providing its ID
    public void deleteProductbyID(String productID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + "=\"" + productID + "\";");
        db.close();
    }

    //DELETE a product by providing its barcode
    public void deleteProductbyBarcode(String barcode) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_BARCODE + "=\"" + barcode + "\";");
        db.close();
    }

    //GET a product name by providing its product_id
    public String getProductName(int productID) {
        String productName = "";
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_PRODUCT_ID + "=\"" + productID + "\";", null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME)) != null) {
                productName = c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME));
            }
            c.moveToNext();
        }
        db.close();
        return productName;
    }

    //GET a product ID by providing its name
    public int getProductID(String productName) {
        int productID = -1;
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_PRODUCT_NAME + "=\"" + productName + "\";", null);
        //Move cursor to the first row
        c.moveToFirst();

        // TODO: Check whether the while loop is necessary or troublesome.
        while (!c.isAfterLast()) {
            if (c.getCount() != 0) {
                productID = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID));
            }
            c.moveToNext();
        }
        db.close();
        return productID;
    }

    //GET all products and store each product_name in an ArrayList
    public ArrayList<String> getProductNames() {
        ArrayList<String> allProducts = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME)) != null) {
                allProducts.add(c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME)));
            }
            c.moveToNext();
        }
        return allProducts;
    }


    //TODO: Print out the table as a String
    /* public String databaseToString(){
        String dbString ="";
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("productname")) != null){
                dbString += c.getString(c.getColumnIndex("productname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    } */
}
