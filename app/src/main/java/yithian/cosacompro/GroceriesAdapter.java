package yithian.cosacompro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.db.dbclasses.GroceriesList;

public class GroceriesAdapter extends BaseAdapter {
    private Context main_context;
    private Activity main_activity;
    private String defaultList;
    private DBPopulator dbPopulator;
    private RowUI rowUI;
    private ArrayList<GroceriesList> groceriesLists;

    public GroceriesAdapter(Context main_context, Activity main_activity, ArrayList<GroceriesList> groceriesLists, String defaultList, DBPopulator dbPopulator) {
        this.main_context = main_context;
        this.main_activity = main_activity;
        this.defaultList = defaultList;
        this.groceriesLists = groceriesLists;
        this.dbPopulator = dbPopulator;
    }

    @Override
    public int getCount() {
        return groceriesLists.size();
    }

    @Override
    public Object getItem(int position) {
        return groceriesLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(main_context).inflate(R.layout.home_row, null);
        }
        // Retrieve the current element (a GroceriesList object)
        GroceriesList curGrocerie = (GroceriesList) getItem(position);

        // Generate the row UI
        rowUI = new RowUI(this, main_activity, view, curGrocerie);
        rowUI.generateRowUI();
        return view;
    }

    public void removeFromList(GroceriesList grocerie) {
        int index = groceriesLists.indexOf(grocerie);
        groceriesLists.remove(index);
    }

    // GETTERS
    public DBPopulator getDbPopulator() {
        return dbPopulator;
    }

    public String getDefaultList() {
        return defaultList;
    }
}