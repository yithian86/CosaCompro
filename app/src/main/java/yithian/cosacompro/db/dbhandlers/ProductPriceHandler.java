package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.ProductPrice;

public class ProductPriceHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "product_price";
    // Table fields
    private static final String COLUMN_PRICELIST_ID = "priceList_id";
    private static final String COLUMN_NORMAL_PRICE = "normal_price";
    private static final String COLUMN_SPECIAL_PRICE = "special_price";
    private static final String COLUMN_SPECIAL_DATE = "special_date";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id_fk";
    private static final String COLUMN_SELLER_ID_FK = "seller_id_fk";
    // Foreign keys
    private static final String PRODUCT_TABLE = "product";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String SELLER_TABLE = "seller";
    private static final String COLUMN_SELLER_ID = "seller_id";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_PRICELIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0), " +
            COLUMN_NORMAL_PRICE + " REAL, " +
            COLUMN_SPECIAL_PRICE + " REAL, " +
            COLUMN_SPECIAL_DATE + " TEXT, " +
            COLUMN_PRODUCT_ID_FK + " INTEGER NOT NULL, " +
            COLUMN_SELLER_ID_FK + " INTEGER NOT NULL, " +
            " FOREIGN KEY (" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + PRODUCT_TABLE + " (" + COLUMN_PRODUCT_ID + "), " +
            " FOREIGN KEY (" + COLUMN_SELLER_ID_FK + ") REFERENCES " + SELLER_TABLE + " (" + COLUMN_SELLER_ID + "));";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_PRODUCT_ID_FK + " COLLATE NOCASE, " + COLUMN_SELLER_ID_FK + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";
    private static ProductPriceHandler mInstance;

    /**
     * Use the application context, which will ensure that you
     * don't accidentally leak an Activity's context.
     * See this article for more information: http://bit.ly/6LRzfx
     */
    public static ProductPriceHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ProductPriceHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private ProductPriceHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    //ADD a new row to the database
    public boolean addProductPrice(ProductPrice productPriceTable) {
        boolean res = false;
        if (productPriceTable != null) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_PRODUCT_ID_FK, productPriceTable.getProduct_id());
                values.put(COLUMN_SELLER_ID_FK, productPriceTable.getSeller_id());
                values.put(COLUMN_NORMAL_PRICE, productPriceTable.getNormal_price());
                values.put(COLUMN_SPECIAL_PRICE, productPriceTable.getSpecial_price());
                values.put(COLUMN_SPECIAL_DATE, productPriceTable.getSpecial_date());
                db.insertOrThrow(TABLE_NAME, null, values);
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.d("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    // UPDATE an existing ProductPrice
    public boolean updateProductPrice(ProductPrice updated_productprice) {
        boolean res = false;
        if (updated_productprice != null) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_PRODUCT_ID_FK, updated_productprice.getProduct_id());
                values.put(COLUMN_SELLER_ID_FK, updated_productprice.getSeller_id());
                values.put(COLUMN_NORMAL_PRICE, updated_productprice.getNormal_price());
                values.put(COLUMN_SPECIAL_PRICE, updated_productprice.getSpecial_price());
                values.put(COLUMN_SPECIAL_DATE, updated_productprice.getSpecial_date());
                db.replaceOrThrow(TABLE_NAME, null, values);
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.d("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    // DELETE all ProductPrice(s) from the DB
    public void deleteAllProductPrices() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // DELETE a ProductPrice
    public void deleteProductPrice(ProductPrice productPrice) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_PRICELIST_ID + "=" + productPrice.getPriceList_id(), null);
        db.close();
    }

    // GET all ProductPrice(s) from the DB
    public ArrayList<ProductPrice> getProductPrices() {
        ArrayList<ProductPrice> resProductList = new ArrayList<ProductPrice>();
        SQLiteDatabase db = getWritableDatabase();
        ProductPrice tempProductPrice;
        int temp_priceList_id, temp_product_id_fk, temp_seller_id_fk;
        double temp_normal_price, temp_special_price;
        String temp_special_date;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " ORDER BY " + COLUMN_PRODUCT_ID_FK, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_priceList_id = c.getInt(c.getColumnIndex(COLUMN_PRICELIST_ID));
            temp_product_id_fk = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID_FK));
            temp_seller_id_fk = c.getInt(c.getColumnIndex(COLUMN_SELLER_ID_FK));
            temp_normal_price = c.getDouble(c.getColumnIndex(COLUMN_NORMAL_PRICE));
            temp_special_price = c.getDouble(c.getColumnIndex(COLUMN_SPECIAL_PRICE));
            temp_special_date = c.getString(c.getColumnIndex(COLUMN_SPECIAL_DATE));
            tempProductPrice = new ProductPrice(temp_priceList_id, temp_product_id_fk, temp_seller_id_fk, temp_normal_price, temp_special_price, temp_special_date);
            resProductList.add(tempProductPrice);
            c.moveToNext();
        }
        c.close();
        db.close();
        return resProductList;
    }
}
