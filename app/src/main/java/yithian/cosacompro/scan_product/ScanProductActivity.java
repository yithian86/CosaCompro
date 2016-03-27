package yithian.cosacompro.scan_product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import yithian.cosacompro.R;
import yithian.cosacompro.db.dbclasses.Product;
import yithian.cosacompro.db.dbhandlers.ProductHandler;
import yithian.cosacompro.manage_products.ProductDetailActivity;

public class ScanProductActivity extends AppCompatActivity {
    // Layouts and containers
    private GridLayout productinfo_layout;
    private GridLayout addProduct_layout;
    // Barcode TextView fields
    private TextView barcodeContent_textview;
    private TextView barcodeFormat_textview;
    // Product detail TextView fields
    private TextView product_name_textview;
    private TextView product_brand_textview;
    private TextView product_category_textview;
    private TextView product_description_textview;
    // addProduct_button
    private Button addProduct_button;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        barcodeContent_textview = (TextView) findViewById(R.id.barcodeContent_textview);
        barcodeFormat_textview = (TextView) findViewById(R.id.barcodeFormat_textview);

        productinfo_layout = (GridLayout) findViewById(R.id.ScanProduct_productinfo_layout);
        addProduct_layout = (GridLayout) findViewById(R.id.ScanProduct_addProduct_layout);

        product_name_textview = (TextView) findViewById(R.id.product_name_textview);
        product_brand_textview = (TextView) findViewById(R.id.product_brand_textview);
        product_category_textview = (TextView) findViewById(R.id.product_category_textview);
        product_description_textview = (TextView) findViewById(R.id.product_description_textview);

        addProduct_button = (Button) findViewById(R.id.addProduct_button);

        // Hide extra layouts and containers
        productinfo_layout.setVisibility(View.GONE);
        addProduct_layout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = "(" + scanningResult.getFormatName() + ")";

            Toast.makeText(getApplicationContext(), "Barcode found: " + scanContent + " - Format: " + scanFormat, Toast.LENGTH_LONG).show();
            barcodeContent_textview.setText(scanContent);
            barcodeFormat_textview.setText(scanFormat);

            product = ProductHandler.getInstance(getApplicationContext()).getProductByBarcode(scanContent);
            if (product != null) {
                // A Product with scanned barcode was found: load it from the DB
                showProductDetails();
            } else if ((scanContent != null) && (!scanContent.equals(""))) {
                // A Product with scanned barcode was NOT found: wanna add it?
                addProduct_layout.setVisibility(View.VISIBLE);
                product = new Product(-1, "", "", "", scanContent, "");
            }
        } else {
            Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_LONG).show();
        }
    }

    public void scanProduct_click(View view) {
        hideProductDetails();
        hideAddProduct();
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    public void addProduct_click(View view){
        addProduct(product, 3);
    }

    private void addProduct(Product product, int open_mode_flag) {
        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
        intent.putExtra("current_product_id", product.getProduct_id());
        intent.putExtra("current_product_name", product.getProduct_name());
        intent.putExtra("current_product_brand", product.getBrand());
        intent.putExtra("current_product_category", product.getCategory());
        intent.putExtra("current_product_barcode", product.getBarcode());
        intent.putExtra("current_product_description", product.getDescription());
        intent.putExtra("open_mode_flag", open_mode_flag);
        startActivity(intent);
    }

    private void hideAddProduct() {
        addProduct_layout.setVisibility(View.GONE);
    }

    private void showProductDetails() {
        // Set the container to Visible
        productinfo_layout.setVisibility(View.VISIBLE);
        // Fill the product details
        product_name_textview.setText(product.getProduct_name());
        product_brand_textview.setText(product.getBrand());
        product_category_textview.setText(product.getCategory());
        product_description_textview.setText(product.getDescription());
    }

    private void hideProductDetails() {
        // Set the container to Visible
        productinfo_layout.setVisibility(View.GONE);
        // Fill the product details
        product_name_textview.setText("-");
        product_brand_textview.setText("-");
        product_category_textview.setText("-");
        product_description_textview.setText("-");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
