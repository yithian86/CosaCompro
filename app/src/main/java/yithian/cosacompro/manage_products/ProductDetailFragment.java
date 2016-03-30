package yithian.cosacompro.manage_products;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import yithian.cosacompro.R;
import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.db.dbclasses.Product;

public class ProductDetailFragment extends Fragment {
    private Product current_product;
    private View rootView;
    private DBHandler dbHandler;
    private ArrayList<String> cat_arraylist;
    private int open_mode_flag;
    // UI stuff
    private TextView prod_name_input;
    private TextView prod_brand_input;
    private Spinner prod_cat_input;
    private TextView prod_barcode_input;
    private TextView prod_desc_input;
    private Button applyProductChanges_button;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("open_mode_flag")) {
            // Load values passed by ProductPriceDetailActivity
            open_mode_flag = getArguments().getInt("open_mode_flag");
            if (open_mode_flag != 2) {
                int product_id = getArguments().getInt("current_product_id");
                String product_name = getArguments().getString("current_product_name");
                String product_brand = getArguments().getString("current_product_brand");
                String product_category = getArguments().getString("current_product_category");
                String product_barcode = getArguments().getString("current_product_barcode");
                String product_description = getArguments().getString("current_product_description");
                current_product = new Product(product_id, product_name, product_brand, product_category, product_barcode, product_description);
            }
            if (open_mode_flag == 3) {
                String product_barcode = getArguments().getString("current_product_barcode");
                current_product = new Product("", "", "", product_barcode, "");
            }

            // prod_cat_input Spinner stuff
            dbHandler = DBHandler.getInstance(this.getContext());
            cat_arraylist = dbHandler.getCategoryHandler().categoryToArrayList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manage_products_detail, container, false);
        // Initialize the UI
        prod_name_input = (TextView) rootView.findViewById(R.id.prod_name_input);
        prod_brand_input = (TextView) rootView.findViewById(R.id.prod_brand_input);
        prod_cat_input = (Spinner) rootView.findViewById(R.id.prod_cat_input);
        prod_barcode_input = (TextView) rootView.findViewById(R.id.prod_barcode_input);
        prod_desc_input = (TextView) rootView.findViewById(R.id.prod_desc_input);
        applyProductChanges_button = (Button) rootView.findViewById(R.id.applyProductChanges_button);

        // Configure the prod_cat_input Spinner
        ArrayAdapter<String> cat_inputadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat_arraylist);
        cat_inputadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prod_cat_input.setAdapter(cat_inputadapter);
        prod_cat_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (open_mode_flag) {
            case 0:
                openReadOnly();
                break;
            case 1:
                openEditMode();
                break;
            case 2:
                openAddMode();
                break;
            case 3:
                prod_barcode_input.setText(current_product.getBarcode());
                openAddMode();
                break;
            default:
                openReadOnly();
                break;
        }
        return rootView;
    }

    private void loadSellerValues() {
        prod_name_input.setText(current_product.getProduct_name());
        prod_brand_input.setText(current_product.getBrand());
        prod_cat_input.setSelection(cat_arraylist.indexOf(current_product.getCategory()));
        prod_barcode_input.setText(current_product.getBarcode());
        prod_desc_input.setText(current_product.getDescription());
    }

    private void triggerUI(boolean trigger) {
        // Trigger input fields
        prod_name_input.setEnabled(trigger);
        prod_brand_input.setEnabled(trigger);
        prod_cat_input.setEnabled(trigger);
        prod_barcode_input.setEnabled(trigger);
        prod_desc_input.setEnabled(trigger);
        // Set focus
        prod_name_input.setFocusable(trigger);
        prod_brand_input.setFocusable(trigger);
        prod_cat_input.setFocusable(trigger);
        prod_barcode_input.setFocusable(trigger);
        prod_desc_input.setFocusable(trigger);
        // Trigger the input button
        if (trigger)
            applyProductChanges_button.setVisibility(View.VISIBLE);
        else applyProductChanges_button.setVisibility(View.GONE);
    }

    private void openReadOnly() {
        if (current_product != null) {
            // Load values in UI
            loadSellerValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_product_info);
            // Disable UI
            triggerUI(false);
        }
    }

    private void openEditMode() {
        if (current_product != null) {
            // Load values in UI
            loadSellerValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_product_edit);
            // Enable UI
            triggerUI(true);

            applyProductChanges_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update current Product
                    current_product.setProduct_name(prod_name_input.getText().toString());
                    current_product.setBrand(prod_brand_input.getText().toString());
                    current_product.setCategory(prod_cat_input.getSelectedItem().toString());
                    current_product.setBarcode(prod_barcode_input.getText().toString());
                    current_product.setDescription(prod_desc_input.getText().toString());
                    // Update the DB
                    boolean updateResult = dbHandler.getProductHandler().updateProduct(current_product);
                    if (!updateResult) {
                        Snackbar.make(getView(), R.string.product_duplicate_error, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        // Go back to the previous screen
                        NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), ProductListActivity.class));
                    }
                }
            });
        }
    }

    private void openAddMode() {
        // Set title on top
        this.getActivity().setTitle(R.string.title_product_add);
        // Enable UI
        triggerUI(true);

        applyProductChanges_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update current Product
                String prod_name = prod_name_input.getText().toString();
                String prod_brand = prod_brand_input.getText().toString();
                String prod_cat = prod_cat_input.getSelectedItem().toString();
                String prod_barcode = prod_barcode_input.getText().toString();
                String prod_desc = prod_desc_input.getText().toString();
                current_product = new Product(prod_name, prod_brand, prod_cat, prod_barcode, prod_desc);
                // Update the DB
                boolean addResult = dbHandler.getProductHandler().addProduct(current_product);
                if (!addResult) {
                    Snackbar.make(getView(), R.string.product_duplicate_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    // Go back to the previous screen
                    NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), ProductListActivity.class));
                }
            }
        });
    }
}
