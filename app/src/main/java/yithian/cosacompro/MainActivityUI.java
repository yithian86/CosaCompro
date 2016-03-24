package yithian.cosacompro;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.db.dbclasses.GList;
import yithian.cosacompro.db.dbclasses.GroceriesList;
import yithian.cosacompro.settings.SettingsManager;

public class MainActivityUI {
    private GList defaultGList;
    private SettingsManager settingsManager;
    private DBPopulator dbPopulator;
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

    public MainActivityUI(Activity main_activity, SettingsManager settingsManager, DBPopulator dbPopulator) {
        this.main_activity = main_activity;
        inputMethodManager = (InputMethodManager) main_activity.getSystemService(Context.INPUT_METHOD_SERVICE);

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

        // BACKUP stuff
        Button DB_Backup_button = (Button) main_activity.findViewById(R.id.DB_Backup_Button);
        DB_Backup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backupDB();
            }
        });
    }

    // Generates the default list view
    public void generateDefaultList() {
        // 1. Update the default list name
        defaultGList = settingsManager.getDefaultList();
        if (defaultGList == null) {
            Log.e("defaultGList", "defaultGList is null");
        } else {
            // 2. Get all the Groceries by providing the GList ID
            ArrayList<GroceriesList> array_GroceriesList = dbPopulator.getGroceriesListHandler().getGroceriesList(defaultGList.getGList_id());
            // 3. Create the ArrayAdapter (layout and content)
            GroceriesAdapter groceriesAdapter = new GroceriesAdapter(main_activity, main_activity, array_GroceriesList, defaultGList.getGList_name(), dbPopulator);
            // 4. Update the list name and apply the ArrayAdapter to the ListView
            listNameView.setText(defaultGList.getGList_name());
            groceriesListView.setAdapter(groceriesAdapter);
        }
    }

    // ======================== BACKUPPONE NAZIONALE ======================== //
    private void backupDB() {
        String backupFileName = "cosacompro.db"; // TODO: Crea un nome diverso per ogni backup (ad es.: "cosacompro_db_backup_20160320.db"
        String dbPath = dbPopulator.getDBPath();
        if (dbPath == null) {
            Toast.makeText(main_activity, "dbPath è null!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Destination directory
            File backupDirectory = Environment.getExternalStoragePublicDirectory("CosaCompro");
//            String path = System.getenv("SECONDARY_STORAGE"); // + "/CosaCompro";
//            File backupDirectory = new File(path);
            backupDirectory.mkdirs();

            if (isExternalStorageAvailable()) {
                // Source DB file path
                File currentDB = new File(dbPath);
                // Destination DB file path
                File backupDB = new File(backupDirectory, backupFileName);
                Log.i("backup dest", backupDirectory.getAbsolutePath());
                // Create Source and Destination channels for transfer
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                // 'DestFileSize' is the number of bytes that are transferred
                long fileSize = dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Fatto", "Porca miseria se l'ho fatto: " + String.valueOf(fileSize) + " Bytes!");
                Toast.makeText(main_activity, "Porca miseria se l'ho fatto!", Toast.LENGTH_LONG).show();
            } else {
                Log.e("Permission denied", "Can't write to SD card, add permission");
                Toast.makeText(main_activity, "Can't write to SD card, add permission", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(main_activity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageAvailable() {
        // Retrieving the external storage state
        String state = Environment.getExternalStorageState();
//        Log.i("state", state);
        // Check if available
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    // ======================== addProductToList ======================== //
    // Generates the layout 'addProductToList', which allows the user to add groceries to the current default list.
    public void generateAddProductToList() {
        if (addProductToListLayout.getVisibility() == View.INVISIBLE) {
            // Generate the Spinner with all the product names.
            ArrayList<String> groceriesNameList = dbPopulator.getGroceriesListHandler().getGroceriesNameByListID(defaultGList.getGList_id());
            ArrayList<String> arrayProducts = differenceList(dbPopulator.getProductHandler().getProductNames(), groceriesNameList);
            ArrayAdapter<String> product_inputAdapter = new ArrayAdapter<String>(main_activity, android.R.layout.simple_spinner_item, arrayProducts);
            product_inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_input.setAdapter(product_inputAdapter);
            product_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                    String productSelected = parent.getItemAtPosition(position).toString();
                    productIdSelected = dbPopulator.getProductHandler().getProductID(productSelected);
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
                    dbPopulator.getGroceriesListHandler().addGroceriesList(new GroceriesList(defaultGList.getGList_id(), productIdSelected, Integer.parseInt(quantityText)));

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
