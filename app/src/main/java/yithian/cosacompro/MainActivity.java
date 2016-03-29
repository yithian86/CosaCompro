package yithian.cosacompro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.dbbackup.DBBackup;
import yithian.cosacompro.manage_glists.GListListActivity;
import yithian.cosacompro.manage_productprices.ProductPriceListActivity;
import yithian.cosacompro.manage_products.ProductListActivity;
import yithian.cosacompro.manage_sellers.SellerListActivity;
import yithian.cosacompro.scan_product.ScanProductActivity;
import yithian.cosacompro.settings.SettingsActivity;
import yithian.cosacompro.settings.SettingsManager;

public class MainActivity extends AppCompatActivity {
    private MainActivityUI mainActivityUI;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Settings and preferences
        SettingsManager settingsManager = new SettingsManager(this);

        // Initialize and populate Database
        dbHandler = DBHandler.getInstance(this);
//        dbHandler.wipeDB();
//        dbHandler.populateDB();

        // Generate UI
        mainActivityUI = new MainActivityUI(this, settingsManager, dbHandler);
        mainActivityUI.generateDefaultList();

        // Bottom-right button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityUI.generateAddProductToList();
            }
        });
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
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_scanProduct:
                startActivity(new Intent(MainActivity.this, ScanProductActivity.class));
                break;
            case R.id.action_manageLists:
                startActivity(new Intent(MainActivity.this, GListListActivity.class));
                break;
            case R.id.action_manageSellers:
                startActivity(new Intent(MainActivity.this, SellerListActivity.class));
                break;
            case R.id.action_manageProducts:
                startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                break;
            case R.id.action_manageProductPrices:
                startActivity(new Intent(MainActivity.this, ProductPriceListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mainActivityUI.generateDefaultList();
    }

    public void exportDB(View view) {
        new DBBackup(this).exportDB();
    }

    public void importDB(View view) {
        new DBBackup(this).importDB();
        mainActivityUI.generateDefaultList();
    }
}
