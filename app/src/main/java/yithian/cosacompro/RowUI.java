package yithian.cosacompro;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.db.dbclasses.GroceriesList;
import yithian.cosacompro.db.dbclasses.Product;

public class RowUI {
    private View view;
    private DBHandler dbHandler;
    private GroceriesList curGroceriesList;

    private View addProductToListLayout;
    private TextView prodNameTextView;
    private TextView quantityTextView;
    private Button incrQButton;
    private Button decrQButton;
    private Button deleteButton;
    private GroceriesAdapter groceriesAdapter;

    public RowUI(GroceriesAdapter groceriesAdapter, DBHandler dbHandler, Activity main_activity, View view, GroceriesList curGroceriesList) {
        this.groceriesAdapter = groceriesAdapter;
        this.dbHandler = dbHandler;
        this.view = view;
        this.curGroceriesList = curGroceriesList;

        // UI elements
        this.addProductToListLayout = main_activity.findViewById(R.id.addProductToListLayout); // TODO: change 'main_activity' with 'view'
        this.deleteButton = (Button) view.findViewById(R.id.deleteButton);
        this.prodNameTextView = (TextView) view.findViewById(R.id.prodNameTextView);
        this.quantityTextView = (TextView) view.findViewById(R.id.quantityTextView);
        this.incrQButton = (Button) view.findViewById(R.id.incrQButton);
        this.decrQButton = (Button) view.findViewById(R.id.decrQButton);
    }

    public void generateRowUI() {
        generateProdNameTextView();
        generateQuantityTextView();
        generateIncrQButton();
        generateDecrQButton();
        generateDeleteButton();
    }

    private void generateProdNameTextView() {
        int product_id = curGroceriesList.getProduct_id();
        Product product = dbHandler.getProductHandler().getProductbyID(product_id);
        prodNameTextView.setText(product.getProduct_name());
    }

    private void generateQuantityTextView() {
        quantityTextView.setText(String.valueOf(curGroceriesList.getQuantity()));
    }

    private void generateIncrQButton() {
        incrQButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 1. Get current quantity and increase it by 1
                int newQuantity = Integer.parseInt(quantityTextView.getText().toString());
                newQuantity++;
                // 2a. Update quantity in TextView value
                quantityTextView.setText(String.valueOf(newQuantity));
                // 2b. Update quantity in the database
                curGroceriesList.setQuantity(newQuantity);
                dbHandler.getGroceriesListHandler().updateQuantity(curGroceriesList);
            }
        });
    }

    private void generateDecrQButton() {
        decrQButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 1. Get current quantity
                int newQuantity = Integer.parseInt(quantityTextView.getText().toString());
                // 2. Check if current quantity can be decreased
                if (newQuantity > 0) {
                    // 3. Decrease it by 1
                    newQuantity--;
                    // 4a. Update quantity in TextView value
                    quantityTextView.setText(String.valueOf(newQuantity));
                    // 4b. Update quantity in the database
                    curGroceriesList.setQuantity(newQuantity);
                    dbHandler.getGroceriesListHandler().updateQuantity(curGroceriesList);
                }
            }
        });
    }

    private void generateDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.getGroceriesListHandler().deleteGrocery(curGroceriesList);
                groceriesAdapter.removeFromList(curGroceriesList);
                groceriesAdapter.notifyDataSetChanged();
                hideAddProductToListLayout();
                triggerDeleteButton();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hideAddProductToListLayout();
                triggerDeleteButton();
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAddProductToListLayout();
                disableDeleteButton();
            }
        });
    }

    private void enableDeleteButton() {
        deleteButton.setEnabled(true);
        deleteButton.setVisibility(View.VISIBLE);
    }

    private void disableDeleteButton() {
        deleteButton.setEnabled(false);
        deleteButton.setVisibility(View.GONE);
    }

    private void triggerDeleteButton() {
        if (deleteButton.isEnabled())
            disableDeleteButton();
        else
            enableDeleteButton();
    }

    private void hideAddProductToListLayout() {
        addProductToListLayout.setVisibility(View.INVISIBLE);
    }
}
