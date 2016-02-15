package yithian.cosacompro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.db.dbclasses.GroceriesList;

public class GroceriesAdapter extends BaseAdapter {
    private DBPopulator dbPopulator;
    private ArrayList<GroceriesList> groceriesLists;
    private Context context;
    private String defaultList;

    public GroceriesAdapter(Context context, ArrayList<GroceriesList> groceriesLists, String defaultList) {
        this.dbPopulator = new DBPopulator(context, null, null, 1);
        this.groceriesLists = groceriesLists;
        this.context = context;
        this.defaultList = defaultList;
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
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row, null);
        }
        final GroceriesList gl = (GroceriesList) getItem(position);
        TextView txt = (TextView) v.findViewById(R.id.prodNameTextView);
        txt.setText(gl.getProduct_name());
        txt = (TextView) v.findViewById(R.id.quantityTextView);
        txt.setText(Integer.toString(gl.getQuantity()));

        // Initialize Buttons
        Button button = (Button) v.findViewById(R.id.incrQButton);
        final TextView finalTxt = txt;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int temp = Integer.parseInt(finalTxt.getText().toString());
                temp++;
                finalTxt.setText(Integer.toString(temp));
                dbPopulator.getGroceriesListHandler().updateQuantity(defaultList, Integer.toString(gl.getProduct_id()), temp);
            }
        });

        button = (Button) v.findViewById(R.id.decrQButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int temp = Integer.parseInt(finalTxt.getText().toString());
                if (temp > 0) {
                    temp--;
                    finalTxt.setText(Integer.toString(temp));
                    dbPopulator.getGroceriesListHandler().updateQuantity(defaultList, Integer.toString(gl.getProduct_id()), temp);
                }
            }
        });
        return v;
    }
}