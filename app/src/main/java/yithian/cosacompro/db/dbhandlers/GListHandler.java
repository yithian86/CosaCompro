package yithian.cosacompro.db.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import yithian.cosacompro.db.dbclasses.GList;

public class GListHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cosacompro.db";
    private static final String TABLE_NAME = "list";
    // Table fields
    private static final String COLUMN_LIST_ID = "glist_id";
    private static final String COLUMN_LIST_NAME = "glist_name";
    private static final String COLUMN_LIST_START = "start";
    private static final String COLUMN_LIST_END = "end";
    // SQL strings
    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "' ( " +
            COLUMN_LIST_ID + " INTEGER PRIMARY KEY NOT NULL DEFAULT (0), " +
            COLUMN_LIST_NAME + " TEXT NOT NULL, " +
            COLUMN_LIST_START + " TEXT, " +
            COLUMN_LIST_END + " TEXT );";
    private static final String SQL_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX " + TABLE_NAME + "unique_constr ON " +
            TABLE_NAME + " (" + COLUMN_LIST_NAME + " COLLATE NOCASE);";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    private static final String SQL_READ_TABLE = "SELECT * FROM '" + TABLE_NAME + "'";

    public GListHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
    public boolean addGList(GList new_GList) {
        boolean res = false;
        if (new_GList != null) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                if (new_GList.getGList_name() != null) {
                    values.put(COLUMN_LIST_NAME, new_GList.getGList_name());
                    if (new_GList.getGList_start() != null)
                        values.put(COLUMN_LIST_START, new_GList.getGList_start());
                    if (new_GList.getGList_end() != null)
                        values.put(COLUMN_LIST_END, new_GList.getGList_end());
                    db.insertOrThrow(TABLE_NAME, null, values);
                    db.close();
                    res = true;
                }
            } catch (SQLiteConstraintException sqlException) {
                Log.d("sqlException", sqlException.getMessage());
                res = false;
            }
        }
        return res;
    }

    // UPDATE an existing GList
    public boolean updateGList(GList updated_glist) {
        boolean res = false;
        if (updated_glist != null) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_LIST_ID, updated_glist.getGList_id());
                values.put(COLUMN_LIST_NAME, updated_glist.getGList_name());
                values.put(COLUMN_LIST_START, updated_glist.getGList_start());
                values.put(COLUMN_LIST_END, updated_glist.getGList_end());
                db.replaceOrThrow(TABLE_NAME, null, values);
                db.close();
                res = true;
            } catch (SQLiteConstraintException sqlException) {
                res = false;
            }
        }
        return res;
    }

    // DELETE a glist from the table
    public void deleteGList(int listID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_LIST_ID + "=" + listID + ";");
        db.close();
    }

    // DELETE a glist from the table
    public void deleteGList(GList gList) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_LIST_ID + "=" + gList.getGList_id() + ";");
        db.close();
    }

    // DELETE all glists from a list
    public void deleteAllGLists() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // GET all glists
    public ArrayList<GList> getGLists() {
        ArrayList<GList> resListGList = new ArrayList<GList>();
        SQLiteDatabase db = getWritableDatabase();
        GList tempGList;
        int temp_list_id;
        String temp_list_name, temp_list_start, temp_list_end;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " ORDER BY " + COLUMN_LIST_NAME + " COLLATE NOCASE", null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            temp_list_id = c.getInt(c.getColumnIndex(COLUMN_LIST_ID));
            temp_list_name = c.getString(c.getColumnIndex(COLUMN_LIST_NAME));
            temp_list_start = c.getString(c.getColumnIndex(COLUMN_LIST_START));
            temp_list_end = c.getString(c.getColumnIndex(COLUMN_LIST_END));
            tempGList = new GList(temp_list_id, temp_list_name, temp_list_start, temp_list_end);
            resListGList.add(tempGList);
            c.moveToNext();
        }
        c.close();
        return resListGList;
    }

    // GET a GList from a given ID
    public GList getGListFromID(int gListID) {
        GList resGList = null;
        SQLiteDatabase db = getWritableDatabase();
        int list_id;
        String list_name, list_start, list_end;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " WHERE " + COLUMN_LIST_ID + "=" + gListID, null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list_id = c.getInt(c.getColumnIndex(COLUMN_LIST_ID));
            list_name = c.getString(c.getColumnIndex(COLUMN_LIST_NAME));
            list_start = c.getString(c.getColumnIndex(COLUMN_LIST_START));
            list_end = c.getString(c.getColumnIndex(COLUMN_LIST_END));
            resGList = new GList(list_id, list_name, list_start, list_end);
            c.moveToNext();
        }
        c.close();
        return resGList;
    }

    //Print out the glist names as ArrayList
    public ArrayList<String> glistToArrayList() {
        ArrayList<String> resList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(SQL_READ_TABLE + " ORDER BY " + COLUMN_LIST_NAME + " COLLATE NOCASE", null);
        //Move cursor to the first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_LIST_NAME)) != null) {
                resList.add(c.getString(c.getColumnIndex(COLUMN_LIST_NAME)));
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return resList;
    }
}
