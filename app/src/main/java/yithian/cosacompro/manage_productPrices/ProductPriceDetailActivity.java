package yithian.cosacompro.manage_productprices;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import yithian.cosacompro.R;

public class ProductPriceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_productprices_activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.notification_text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            if (getIntent().getIntExtra("open_mode_flag", 0) != 2) {
                arguments.putInt("cur_productprice_id", getIntent().getIntExtra("cur_productprice_id", -1));
                arguments.putInt("cur_productprice_prod", getIntent().getIntExtra("cur_productprice_prod", -1));
                arguments.putInt("cur_productprice_seller", getIntent().getIntExtra("cur_productprice_seller", -1));
                arguments.putDouble("cur_productprice_norm", getIntent().getDoubleExtra("cur_productprice_norm", -1));
                arguments.putDouble("cur_productprice_spec", getIntent().getDoubleExtra("cur_productprice_spec", -1));
                arguments.putString("cur_productprice_specdate", getIntent().getStringExtra("cur_productprice_specdate"));
            }
            arguments.putInt("open_mode_flag", getIntent().getIntExtra("open_mode_flag", 0));

            ProductPriceDetailFragment fragment = new ProductPriceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.productprice_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Go back (home)
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ProductPriceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
