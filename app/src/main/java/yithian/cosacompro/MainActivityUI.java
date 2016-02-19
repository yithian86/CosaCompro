package yithian.cosacompro;


import android.app.Activity;
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

import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.db.dbclasses.GroceriesList;

public class MainActivityUI {
    private String defaultList;
    private SettingsManager settingsManager;
    private DBPopulator dbPopulator;
    private Activity main_activity;
    // Default list fields
    private ArrayList<String> groceriesNameList;
    private ArrayList<Integer> groceriesIdList;
    private ArrayList<String> quantityList;
    private ArrayList<GroceriesList> arrGrList;
    private ListView groceriesListView;
    private TextView listNameView;
    private GroceriesAdapter groceriesAdapter;
    // addProductToList fields
    private View addProductToListLayout;
    private Spinner product_input;
    private EditText quantity_input;
    private Button ok_button;
    private ArrayList<String> arrayProducts;
    private ArrayAdapter<String> product_inputAdapter;
    private InputMethodManager inputMethodManager;

    public MainActivityUI(Activity main_activity, SettingsManager settingsManager, DBPopulator dbPopulator) {
        this.main_activity = main_activity;
        inputMethodManager = (InputMethodManager) main_activity.getSystemService(main_activity.INPUT_METHOD_SERVICE);

        // Settings and Preferences
        this.settingsManager = settingsManager;

        // DBPopulator
        this.dbPopulator = dbPopulator;

        // Default List UI stuff
        listNameView = (TextView) main_activity.findViewById(R.id.listNameView);
        groceriesListView = (ListView) main_activity.findViewById(R.id.gListView);

        // AddProductToList UI stuff
        addProductToListLayout = main_activity.findViewById(R.id.addProductToListLayout);
        product_input = (Spinner) main_activity.findViewById(R.id.product_input);
        quantity_input = (EditText) main_activity.findViewById(R.id.quantity_input);
        ok_button = (Button) main_activity.findViewById(R.id.ok_button);
    }

    // Generates the default list view
    public void generateDefaultList() {
        // 1. Update the default list name
        defaultList = settingsManager.getDefaultList();
        // 2. Get the groceries list by providing the list name
        groceriesNameList = dbPopulator.getGroceriesListHandler().getGroceriesNameByListName(defaultList);
        groceriesIdList = dbPopulator.getGroceriesListHandler().getGroceriesIdByListName(defaultList);
        quantityList = dbPopulator.getGroceriesListHandler().getQuantitiesByListName(defaultList);
        arrGrList = new ArrayList<GroceriesList>();
        GroceriesList gl;
        for (int i = 0; i < groceriesNameList.size(); i++) {
            gl = new GroceriesList(Integer.parseInt(quantityList.get(i)), defaultList, groceriesIdList.get(i).intValue(), groceriesNameList.get(i));
            arrGrList.add(gl);
        }
        // 3. Create the ArrayAdapter (layout and content)
        groceriesAdapter = new GroceriesAdapter(main_activity, arrGrList, defaultList);
        // 4. Update the list name and apply the ArrayAdapter to the ListView
        listNameView.setText(defaultList);
        groceriesListView.setAdapter(groceriesAdapter);
    }

    // Generates the layout 'addProductToList', which allows the user to add groceries to the current default list.
    public void generateAddProductToList() {
        final int[] productIdSelected = new int[1];

        if (addProductToListLayout.getVisibility() == View.INVISIBLE) {
            // Generate the Spinner with all the product names.
            arrayProducts = differenceList(dbPopulator.getProductHandler().getProductNames(), groceriesNameList);
            product_inputAdapter = new ArrayAdapter<String>(main_activity, android.R.layout.simple_spinner_item, arrayProducts);
            product_inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_input.setAdapter(product_inputAdapter);
            product_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String productSelected = parent.getItemAtPosition(position).toString();
                    productIdSelected[0] = dbPopulator.getProductHandler().getProductID(productSelected);
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
                    dbPopulator.getGroceriesListHandler().addGroceriesList(new GroceriesList(Integer.parseInt(quantityText), defaultList, productIdSelected[0]));
                    clearAddProductToListFields();
                    inputMethodManager.hideSoftInputFromWindow(main_activity.getCurrentFocus().getWindowToken(), 0);
                    addProductToListLayout.setVisibility(View.INVISIBLE);
                    generateDefaultList();
                }
            });
            // Make the layout visibile
            addProductToListLayout.setVisibility(View.VISIBLE);
        } else {
            clearAddProductToListFields();
            inputMethodManager.hideSoftInputFromWindow(main_activity.getCurrentFocus().getWindowToken(), 0);
            addProductToListLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void clearAddProductToListFields() {
        product_input.setSelection(-1);
        quantity_input.setText("0");
    }

    // minuend - subtraend = difference
    private ArrayList<String> differenceList(ArrayList<String> minuend, ArrayList<String> subtraend) {
        ArrayList<String> differenceList = new ArrayList<>();
        if (minuend.size() <= subtraend.size()) {
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
