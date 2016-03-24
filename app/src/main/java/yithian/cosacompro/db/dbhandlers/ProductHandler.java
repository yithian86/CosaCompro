package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            " FOREIGN KEY (" + COLUMN_CATEGORY + ") REFERENCES " + CATEGORY_TABLE + " (" + COLUMN_CAT_NAME + "));";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_PRODUCT_NAME + " COLLATE NOCASE, " + COLUMN_BRAND + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public ProductHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_UNIQUE_CONSTRAINT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // ADD a new row to the database
    public boolean addProduct(Product product) {
        boolean res = false;
        if (product != null) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_BARCODE, product.getBarcode());
                values.put(COLUMN_PRODUCT_NAME, product.getProduct_name());
                values.put(COLUMN_BRAND, product.getBrand());
                values.put(COLUMN_DESCRIPTION, product.getDescription());
                values.put(COLUMN_CATEGORY, product.getCategory());
                db.insertOrThrow(TABLE_NAME, null, values);
                db.close();
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.d("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    // UPDATE an existing Product
    public boolean updateProduct(Product updated_product) {
        boolean res = false;
        if (updated_product != null) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_BARCODE, updated_product.getBarcode());
                values.put(COLUMN_PRODUCT_NAME, updated_product.getProduct_name());
                values.put(COLUMN_BRAND, updated_product.getBrand());
                values.put(COLUMN_DESCRIPTION, updated_product.getDescription());
                values.put(COLUMN_CATEGORY, updated_product.getCategory());

                db.replaceOrThrow(TABLE_NAME, null, values);
                db.close();
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.d("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    // DELETE all products from the DB
    public void deleteAllProducts() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // DELETE a product
    public void deleteProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_PRODUCT_ID + "=" + product.getProduct_id(), null);
        db.close();
    }

    // GET all Products
    public ArrayList<Product> getProducts() {
        ArrayList<Product> resProductList = new ArrayList<Product>();
        SQLiteDatabase db = getWritableDatabase();
        Product tempProduct;
        int temp_product_id;
        String temp_product_name, temp_brand, temp_description, temp_barcode, temp_category;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " ORDER BY " + COLUMN_PRODUCT_NAME, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_product_id = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID));
            temp_product_name = c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME));
            temp_brand = c.getString(c.getColumnIndex(COLUMN_BRAND));
            temp_barcode = c.getString(c.getColumnIndex(COLUMN_BARCODE));
            temp_category = c.getString(c.getColumnIndex(COLUMN_CATEGORY));
            temp_description = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
            tempProduct = new Product(temp_product_id, temp_product_name, temp_brand, temp_category, temp_barcode, temp_description);
            resProductList.add(tempProduct);
            c.moveToNext();
        }
        c.close();
        db.close();
        return resProductList;
    }

    // GET a Product by providing its ID
    public Product getProductbyID(int productID) {
        Product resProduct = null;
        SQLiteDatabase db = getWritableDatabase();
        int temp_product_id;
        String temp_product_name, temp_brand, temp_description, temp_barcode, temp_category;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_PRODUCT_ID + "=\"" + productID + "\";", null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_product_id = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID));
            temp_product_name = c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME));
            temp_brand = c.getString(c.getColumnIndex(COLUMN_BRAND));
            temp_barcode = c.getString(c.getColumnIndex(COLUMN_BARCODE));
            temp_category = c.getString(c.getColumnIndex(COLUMN_CATEGORY));
            temp_description = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
            resProduct = new Product(temp_product_id, temp_product_name, temp_brand, temp_category, temp_barcode, temp_description);
            c.moveToNext();
        }
        c.close();
        db.close();
        return resProduct;
    }

    // GET a product ID by providing its name
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
        c.close();
        db.close();
        return productID;
    }

    // GET a product name by providing its ID
    public String getProductNameByID(int productID) {
        String productName = "";
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_PRODUCT_ID + "=\"" + productID + "\";", null);
        //Move cursor to the first row
        c.moveToFirst();

        // TODO: Check whether the while loop is necessary or troublesome.
        if ((!c.isAfterLast() && (c.getCount() != 0))) {
            productName = c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME));
        }
        c.close();
        db.close();
        return productName;
    }

    // GET all products and store each product_name in an ArrayList
    public ArrayList<String> getProductNames() {
        ArrayList<String> allProducts = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " ORDER BY " + COLUMN_PRODUCT_NAME, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME)) != null) {
                allProducts.add(c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME)));
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return allProducts;
    }
}
