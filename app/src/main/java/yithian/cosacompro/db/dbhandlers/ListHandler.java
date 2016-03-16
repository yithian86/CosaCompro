package yithian.cosacompro.db.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.List;

public class ListHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "list";
    // Table fields
    private static final String COLUMN_LIST_NAME = "list_name";
    private static final String COLUMN_LIST_START = "start";
    private static final String COLUMN_LIST_END = "end";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_LIST_NAME + " TEXT PRIMARY KEY NOT NULL, " +
            COLUMN_LIST_START + " TEXT, " +
            COLUMN_LIST_END + " TEXT );";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_LIST_NAME + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public ListHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
    public void addList(List list) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_NAME, list.getList_name());
        if (list.getStart() != null) values.put(COLUMN_LIST_START, list.getStart().toString());
        if (list.getEnd() != null) values.put(COLUMN_LIST_END, list.getEnd().toString());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // DELETE a list from the table
    public void deleteList(String listName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_LIST_NAME + "=\"" + listName + "\";");
        db.close();
    }

    // DELETE all products from a list
    public void deleteAllLists() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    //Print out the list names as ArrayList
    public ArrayList<String> listToArrayList() {
        ArrayList<String> resList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("list_name")) != null) {
                resList.add(c.getString(c.getColumnIndex("list_name")));
            }
            c.moveToNext();
        }
        db.close();
        return resList;
    }
}
