package yithian.cosacompro.db.dbhandlers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
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
    private static final String COLUMN_LIST_ID_FK = "list_id_fk";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id_fk";
    private static final String COLUMN_QUANTITY = "quantity";
    // Foreign keys
    private static final String LIST_TABLE = "list";
    private static final String COLUMN_LIST_ID = "glist_id";
    private static final String PRODUCT_TABLE = "product";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_GLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0), " +
            COLUMN_QUANTITY + " INTEGER DEFAULT (1), " +
            COLUMN_PRODUCT_ID_FK + " INTEGER NOT NULL, " +
            COLUMN_LIST_ID_FK + " INTEGER NOT NULL, " +
            " FOREIGN KEY (" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + LIST_TABLE + " (" + COLUMN_LIST_ID + "), " +
            " FOREIGN KEY (" + COLUMN_LIST_ID_FK + ") REFERENCES " + PRODUCT_TABLE + " (" + COLUMN_PRODUCT_ID + "));";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_PRODUCT_ID_FK + " COLLATE NOCASE, " + COLUMN_LIST_ID_FK + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public GroceriesListHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    //ADD a new row to the database
    public boolean addGroceriesList(GroceriesList new_GrocerysList) {
        boolean res = false;
        if (new_GrocerysList != null) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_LIST_ID_FK, new_GrocerysList.getList_id());
                values.put(COLUMN_PRODUCT_ID_FK, new_GrocerysList.getProduct_id());
                values.put(COLUMN_QUANTITY, new_GrocerysList.getQuantity());
                db.insertOrThrow(TABLE_NAME, null, values);
                db.close();
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.e("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    //UPDATE quantity
    public boolean updateQuantity(GroceriesList updated_GroceriesList) {
        boolean res = false;
        if (updated_GroceriesList != null) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_GLIST_ID, updated_GroceriesList.getGroceriesList_id());
                values.put(COLUMN_LIST_ID_FK, updated_GroceriesList.getList_id());
                values.put(COLUMN_PRODUCT_ID_FK, updated_GroceriesList.getProduct_id());
                values.put(COLUMN_QUANTITY, updated_GroceriesList.getQuantity());
                db.replaceOrThrow(TABLE_NAME, null, values);
                db.close();
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                Log.e("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
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
        db.delete(TABLE_NAME, COLUMN_GLIST_ID + "=" + grocery.getGroceriesList_id(), null);
        db.close();
    }

    //DELETE all groceries from a list
    public void deleteAllGroceriesFromList(int list_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_LIST_ID_FK + "=" + list_id + ";");
        db.close();
    }

    // GET all Groceries belonging to a specific GList
    public ArrayList<GroceriesList> getGroceriesList(int list_id) {
        ArrayList<GroceriesList> resListGList = new ArrayList<GroceriesList>();
        SQLiteDatabase db = getWritableDatabase();
        GroceriesList tempGList;
        int temp_glist_id, temp_list_id, temp_product_id, temp_quantity;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_ID_FK + "=" + list_id + " ORDER BY " + COLUMN_GLIST_ID + " COLLATE NOCASE", null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_glist_id = c.getInt(c.getColumnIndex(COLUMN_GLIST_ID));
            temp_list_id = c.getInt(c.getColumnIndex(COLUMN_LIST_ID_FK));
            temp_product_id = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID_FK));
            temp_quantity = c.getInt(c.getColumnIndex(COLUMN_QUANTITY));
            tempGList = new GroceriesList(temp_glist_id, temp_list_id, temp_product_id, temp_quantity);
            resListGList.add(tempGList);
            c.moveToNext();
        }
        c.close();
        return resListGList;
    }

    // GET the product name(s) of the given list listName
    public ArrayList<String> getGroceriesNameByListID(int list_id) {
        ArrayList<String> resList = new ArrayList<String>();
        ArrayList<Integer> product_id = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_ID_FK + "=" + list_id + " ORDER BY " + COLUMN_GLIST_ID + " ASC", null);
        //Move cursor to the first row
        c.moveToFirst();
        while (!c.isAfterLast()) {
            product_id.add(c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID_FK)));
            c.moveToNext();
        }
        c.close();

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
        cur.close();
        return resList;
    }
}