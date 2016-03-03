package yithian.cosacompro;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yithian.cosacompro.db.dbclasses.GroceriesList;
import yithian.cosacompro.db.dbhandlers.GroceriesListHandler;

public class RowUI {
    private GroceriesAdapter groceriesAdapter;
    private View view;
    private GroceriesList curGroceriesList;
    private GroceriesListHandler groceriesListHandler;

    private View addProductToListLayout;
    private TextView prodNameTextView;
    private TextView quantityTextView;
    private Button incrQButton;
    private Button decrQButton;
    private Button deleteButton;

    public RowUI(GroceriesAdapter groceriesAdapter, Activity main_activity, View view, GroceriesList curGroceriesList) {
        this.groceriesAdapter = groceriesAdapter;
        this.view = view;
        this.curGroceriesList = curGroceriesList;
        this.groceriesListHandler = groceriesAdapter.getDbPopulator().getGroceriesListHandler();

        // UI elements
        this.addProductToListLayout = (View) main_activity.findViewById(R.id.addProductToListLayout);
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
        prodNameTextView.setText(curGroceriesList.getProduct_name());
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
                quantityTextView.setText(Integer.toString(newQuantity));
                // 2b. Update quantity in the database
                curGroceriesList.setQuantity(newQuantity);
                groceriesListHandler.updateQuantity(curGroceriesList);
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
                    quantityTextView.setText(Integer.toString(newQuantity));
                    // 4b. Update quantity in the database
                    curGroceriesList.setQuantity(newQuantity);
                    groceriesListHandler.updateQuantity(curGroceriesList);
                }
            }
        });
    }

    private void generateDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Removing: " + curGroceriesList.getProduct_id() + ":" + curGroceriesList.getProduct_name() + " - " + curGroceriesList.getList_name() + "\n");

                groceriesListHandler.deleteGrocery(curGroceriesList);
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
