package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.GroceriesList;

public class GroceriesListHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "groceriesList";
    // Table fields
    private static final String COLUMN_GLIST_ID = "glist_id";
    private static final String COLUMN_LIST_NAME_FK = "list_name_fk";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id_fk";
    private static final String COLUMN_QUANTITY = "quantity";
    // Foreign keys
    private static final String LIST_TABLE = "list";
    private static final String COLUMN_LIST_NAME = "list_name";
    private static final String PRODUCT_TABLE = "product";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_GLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0), " +
            COLUMN_QUANTITY + " INTEGER DEFAULT (1), " +
            COLUMN_PRODUCT_ID_FK + " INTEGER NOT NULL, " +
            COLUMN_LIST_NAME_FK + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + LIST_TABLE + " (" + COLUMN_LIST_NAME + "), " +
            " FOREIGN KEY (" + COLUMN_LIST_NAME_FK + ") REFERENCES " + PRODUCT_TABLE + " (" + COLUMN_PRODUCT_ID + "));";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_PRODUCT_ID_FK + " COLLATE NOCASE, " + COLUMN_LIST_NAME_FK + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public GroceriesListHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_UNIQUE_CONSTRAINT);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //ADD a new row to the database
    public void addGroceriesList(GroceriesList grocery) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, grocery.getQuantity());
        values.put(COLUMN_PRODUCT_ID_FK, grocery.getProduct_id());
        values.put(COLUMN_LIST_NAME_FK, grocery.getList_name());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //UPDATE quantity
    public void updateQuantity(GroceriesList grocery) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, grocery.getQuantity());
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, COLUMN_LIST_NAME_FK + "= \"" + grocery.getList_name() + "\" AND "
                + COLUMN_PRODUCT_ID_FK + "=" + grocery.getProduct_id(), null);
    }

    //DELETE all groceries from the DB
    public void deleteAllGroceries() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //DELETE a grocery from the list
    public void deleteGrocery(GroceriesList grocery) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_LIST_NAME_FK + "=\"" + grocery.getList_name() + "\" AND "
                + COLUMN_PRODUCT_ID_FK + "=\"" + grocery.getProduct_id() + "\"", null);
        db.close();
    }

    //DELETE all groceries from a list
    public void deleteAllGroceriesFromList(String listName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_LIST_NAME_FK + "=\"" + listName + "\";");
        db.close();
    }

    //GET the product name(s) of the given list listName
    public ArrayList<String> getGroceriesNameByListName(String listName) {
        ArrayList<String> resList = new ArrayList<String>();
        ArrayList<Integer> product_id = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_NAME_FK + "=\"" + listName + "\" ORDER BY \"" + COLUMN_GLIST_ID + "\" ASC", null);
        //Move cursor to the first row
        c.moveToFirst();
        while (!c.isAfterLast()) {
            product_id.add(c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID_FK)));
            c.moveToNext();
        }

        Cursor cur = db.rawQuery("SELECT * FROM " + PRODUCT_TABLE + ";", null);
        for (int i = 0; i < product_id.size(); i++) {
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (cur.getInt(cur.getColumnIndex("product_id")) == product_id.get(i)) {
                    resList.add(cur.getString(cur.getColumnIndex("product_name")));
                }
                cur.moveToNext();
            }
        }
        db.close();
        return resList;
    }

    //GET the product id(s) of the given list listName
    public ArrayList<Integer> getGroceriesIdByListName(String listName) {
        ArrayList<Integer> resList = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_NAME_FK + "=\"" + listName + "\" ORDER BY \"" + COLUMN_GLIST_ID + "\" ASC", null);
        //Move cursor to the first row
        c.moveToFirst();
        while (!c.isAfterLast()) {
            resList.add(c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID_FK)));
            c.moveToNext();
        }
        db.close();
        return resList;
    }

    //GET the quantities of the given list listName
    public ArrayList<String> getQuantitiesByListName(String listName) {
        ArrayList<String> resList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_NAME_FK + "=\"" + listName + "\" ORDER BY \"" + COLUMN_GLIST_ID + "\" ASC", null);
        //Move cursor to the first row
        c.moveToFirst();
        while (!c.isAfterLast()) {
            resList.add(c.getString(c.getColumnIndex(COLUMN_QUANTITY)));
            c.moveToNext();
        }
        db.close();
        return resList;
    }

    // TODO: Put test purpose methods here
    public void logGroceriesList(String function, String listName) {
        ArrayList<String> a = getGroceriesNameByListName(listName);
        for (String s : a) {
            Log.d(function, s);
        }
        Log.d(function, "-----------------------------");
    }
}
