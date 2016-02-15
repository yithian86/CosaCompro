package yithian.cosacompro.db.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.Category;

public class CategoryHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "category";
    // Table fields
    private static final String COLUMN_CAT_NAME = "cat_name";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_CAT_NAME + " TEXT PRIMARY KEY NOT NULL " +
            ");";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public CategoryHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
    public void addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAT_NAME, category.getCat_name());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // DELETE a category from the table
    public void deleteCategory(String categoryName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_CAT_NAME + "=\"" + categoryName + "\";");
        db.close();
    }

    // DELETE all categories from the table
    public void deleteAllCategories() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //Print out the categories as ArrayList
    public ArrayList<String> categoryToArrayList() {
        String temp = "";
        ArrayList<String> resList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp = c.getString(c.getColumnIndex("cat_name"));
            resList.add(temp);
            c.moveToNext();
        }
        db.close();
        return resList;
    }
}
