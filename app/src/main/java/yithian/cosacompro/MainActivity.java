package yithian.cosacompro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import yithian.cosacompro.db.dbhandlers.GroceriesListHandler;

public class MainActivity extends AppCompatActivity {
    private String defaultList;
    private SettingsManager settingsManager;
    private DBPopulator dbPopulator;
    private GroceriesListHandler groceriesListHandler;

    private ArrayList<String> groceriesNameList;
    private ArrayList<Integer> groceriesIdList;
    private ArrayList<String> quantityList;
    private ArrayList<GroceriesList> arrGrList;
    private ListView groceriesListView;
    private TextView listNameView;
    private GroceriesAdapter groceriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Settings and Preferences
        settingsManager = new SettingsManager(this);

        // Initialize and populate Database
        dbPopulator = new DBPopulator(this, null, null, 1);
        dbPopulator.populateDB();
        groceriesListHandler = dbPopulator.getGroceriesListHandler();

        // Initialize the default list view on the main page
        refreshDefaultList();

        // Bottom-right button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToList();
//                Snackbar.make(view, "eeeeh!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void refreshDefaultList() {
        // 1. Update the default list name
        defaultList = settingsManager.getDefaultList();
        // 2. Grab the listNameView and the gListView
        listNameView = (TextView) findViewById(R.id.listNameView);
        groceriesListView = (ListView) findViewById(R.id.gListView);
        // 3. Get the groceries list by providing the list name
        groceriesNameList = groceriesListHandler.getGroceriesNameByListName(defaultList);
        groceriesIdList = groceriesListHandler.getGroceriesIdByListName(defaultList);
        quantityList = groceriesListHandler.getQuantitiesByListName(defaultList);
        arrGrList = new ArrayList<GroceriesList>();
        GroceriesList gl;
        for (int i = 0; i < groceriesNameList.size(); i++) {
            gl = new GroceriesList(Integer.parseInt(quantityList.get(i)), defaultList, groceriesIdList.get(i).intValue(), groceriesNameList.get(i));
            arrGrList.add(gl);
        }
        // 4. Create the ArrayAdapter (layout and content)
        groceriesAdapter = new GroceriesAdapter(this, arrGrList, defaultList);
        // 5. Update the list name and apply the ArrayAdapter to the ListView
        listNameView.setText(defaultList);
        groceriesListView.setAdapter(groceriesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void addProductToList() {
        final int[] productIdSelected = new int[1];
        final View v = findViewById(R.id.addProductToListLayout);
        if (v.getVisibility() == View.GONE) {
            /*
            Generate the Spinner with all the product names.
            TODO: Exclude from the spinner all products that are already in the list. In order to do that
            TODO: you should create a specific method in the GroceriesListHandler which takes in input
            TODO: an ArrayList<String> containing all products from the default_list and the default_list name.
            */
            ArrayList<String> arrayProducts = dbPopulator.getProductHandler().getProductNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, arrayProducts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner sItems = (Spinner) findViewById(R.id.product_input);
            sItems.setAdapter(adapter);
            sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String productSelected = parent.getItemAtPosition(position).toString();
                    productIdSelected[0] = dbPopulator.getProductHandler().getProductID(productSelected);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // Configure the button thingy
            Button ok_button = (Button) findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText quantity_input = (EditText) findViewById(R.id.quantity_input);
                    String quantityText = quantity_input.getText().toString();
                    groceriesListHandler.addGroceriesList(new GroceriesList(Integer.parseInt(quantityText), defaultList, productIdSelected[0]));
                    // TODO: Clear the input text fields. You may wanna create a function to do that
                    // TODO: and maybe incapsulate this horrorific mess inside a unique class (maybe a MainActivityUI class?).
                    v.setVisibility(View.GONE);
                    refreshDefaultList();
                }
            });

            // Make the layout visibile
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
            Spinner sItems = (Spinner) findViewById(R.id.product_input);
            EditText editText = (EditText) findViewById(R.id.quantity_input);
            editText.setText("");
            InputMethodManager i = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshDefaultList();
    }
}
