package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yithian.cosacompro.db.dbclasses.ProductPrice;

public class ProductPriceHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "priceList";
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
            " FOREIGN KEY (" + COLUMN_SELLER_ID_FK + ") REFERENCES " + SELLER_TABLE + " (" + COLUMN_SELLER_ID + ") " +
            ");";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";
    private ProductPrice productPriceTable;

    public ProductPriceHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
    public void addProductPrice(ProductPrice productPriceTable) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID_FK, productPriceTable.getProduct_id());
        values.put(COLUMN_NORMAL_PRICE, productPriceTable.getNormal_price());
        values.put(COLUMN_SPECIAL_PRICE, productPriceTable.getSpecial_price());
        values.put(COLUMN_SPECIAL_DATE, productPriceTable.getSpecial_date().toString());
        values.put(COLUMN_SELLER_ID_FK, productPriceTable.getSeller_id());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // DELETE all products from the DB
    public void deleteAllProductPrices() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //DELETE a product by providing its ID
    public void deleteProductPricebyID(String productPriceID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + "=\"" + productPriceID + "\";");
        db.close();
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
