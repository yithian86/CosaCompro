package yithian.cosacompro;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.db.dbclasses.GList;
import yithian.cosacompro.db.dbclasses.GroceriesList;
import yithian.cosacompro.settings.SettingsManager;

public class MainActivityUI {
    private GList defaultGList;
    private SettingsManager settingsManager;
    private DBHandler dbHandler;
    private Activity main_activity;
    private ListView groceriesListView;
    private TextView listNameView;
    // addProductToList fields
    private View addProductToListLayout;
    private Spinner product_input;
    private EditText quantity_input;
    private Button ok_button;
    private InputMethodManager inputMethodManager;
    private int productIdSelected;

    public MainActivityUI() {
    }

    public MainActivityUI(Activity main_activity, SettingsManager settingsManager, DBHandler dbHandler) {
        this.main_activity = main_activity;
        inputMethodManager = (InputMethodManager) main_activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Settings and Preferences
        this.settingsManager = settingsManager;

        // DBPopulator
        this.dbHandler = dbHandler;

        // Default List UI stuff
        listNameView = (TextView) main_activity.findViewById(R.id.listNameView);
        groceriesListView = (ListView) main_activity.findViewById(R.id.gListView);

        // AddProductToList UI stuff
        addProductToListLayout = main_activity.findViewById(R.id.addProductToListLayout);
        product_input = (Spinner) main_activity.findViewById(R.id.product_input);
        quantity_input = (EditText) main_activity.findViewById(R.id.quantity_input);
        ok_button = (Button) main_activity.findViewById(R.id.ok_button);
        close_AddProductToListLayout();
    }

    // Generates the default list view
    public void generateDefaultList() {
        // 1. Update the default list name
        defaultGList = settingsManager.getDefaultList();
        if (defaultGList == null) {
            Log.e("defaultGList", "defaultGList is null");
        } else {
            // 2. Get all the Groceries by providing the GList ID
            ArrayList<GroceriesList> array_GroceriesList = dbHandler.getGroceriesListHandler().getGroceriesList(defaultGList.getGList_id());
            // 3. Create the ArrayAdapter (layout and content)
            GroceriesAdapter groceriesAdapter = new GroceriesAdapter(main_activity, main_activity, array_GroceriesList, defaultGList.getGList_name(), dbHandler);
            // 4. Update the list name and apply the ArrayAdapter to the ListView
            listNameView.setText(defaultGList.getGList_name());
            groceriesListView.setAdapter(groceriesAdapter);
        }
    }

    // ======================== addProductToList ======================== //
    // Generates the layout 'addProductToList', which allows the user to add groceries to the current default list.
    public void generateAddProductToList() {
        if (addProductToListLayout.getVisibility() != View.VISIBLE) {
            // Generate the Spinner with all the product names.
            ArrayList<String> groceriesNameList = dbHandler.getGroceriesListHandler().getGroceriesNameByListID(defaultGList.getGList_id());
            ArrayList<String> arrayProducts = differenceList(dbHandler.getProductHandler().getProductNames(), groceriesNameList);
            ArrayAdapter<String> product_inputAdapter = new ArrayAdapter<String>(main_activity, android.R.layout.simple_spinner_item, arrayProducts);
            product_inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_input.setAdapter(product_inputAdapter);
            product_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                    String productSelected = parent.getItemAtPosition(position).toString();
                    productIdSelected = dbHandler.getProductHandler().getProductID(productSelected);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Configure the Ok_button thingy
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantityText = quantity_input.getText().toString();
                    dbHandler.getGroceriesListHandler().addGroceriesList(new GroceriesList(defaultGList.getGList_id(), productIdSelected, Integer.parseInt(quantityText)));
                    close_AddProductToListLayout();
                    generateDefaultList();
                }
            });
            // Make the layout visibile
            addProductToListLayout.setVisibility(View.VISIBLE);
        } else {
            close_AddProductToListLayout();
        }
    }

    private void close_AddProductToListLayout() {
        product_input.setSelection(-1);
        quantity_input.setText("0");
        addProductToListLayout.setVisibility(View.GONE);
        try {
            inputMethodManager.hideSoftInputFromWindow(main_activity.getCurrentFocus().getWindowToken(), 0);
        } catch (java.lang.NullPointerException e) {
            Log.e(e.getMessage(), e.toString());
        }
    }

    // minuend - subtraend = difference
    private ArrayList<String> differenceList(ArrayList<String> minuend, ArrayList<String> subtraend) {
        ArrayList<String> differenceList = new ArrayList<>();
        if (subtraend.size() == 0)
            return minuend;
        else if (minuend.size() <= subtraend.size()) {
            differenceList.add("Nessun prodotto disponibile!");
            disableOk_button();
        } else {
            for (int i = 0; i < minuend.size(); i++) {
                if (!subtraend.contains(minuend.get(i)))
                    differenceList.add(minuend.get(i));
            }
            enableOk_button();
        }
        return differenceList;
    }

    private void disableOk_button() {
        ok_button.setEnabled(false);
    }

    private void enableOk_button() {
        ok_button.setEnabled(true);
    }
}
