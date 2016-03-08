package yithian.cosacompro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import yithian.cosacompro.db.DBPopulator;
import yithian.cosacompro.manage_products.ProductListActivity;
import yithian.cosacompro.manage_sellers.SellerListActivity;
import yithian.cosacompro.settings.SettingsActivity;
import yithian.cosacompro.settings.SettingsManager;

public class MainActivity extends AppCompatActivity {
    private SettingsManager settingsManager;
    private DBPopulator dbPopulator;
    private MainActivityUI mainActivityUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Settings and preferences
        settingsManager = new SettingsManager(this);
        // Initialize and populate Database
        dbPopulator = new DBPopulator(this, null, null, 1);
        dbPopulator.populateDB();
        // Generate UI
        mainActivityUI = new MainActivityUI(this, settingsManager, dbPopulator);
        mainActivityUI.generateDefaultList();

        // Bottom-right button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityUI.generateAddProductToList();
//                Snackbar.make(view, "eeeeh!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
        Intent intent;

        // noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_manageSellers:
                intent = new Intent(MainActivity.this, SellerListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_manageProducts:
                intent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mainActivityUI.generateDefaultList();
    }
}
