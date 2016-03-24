package yithian.cosacompro.manage_sellers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import yithian.cosacompro.R;
import yithian.cosacompro.db.DBHandler;
import yithian.cosacompro.db.dbclasses.Seller;

/**
 * A fragment representing a single Seller detail screen.
 * This fragment is either contained in a {@link SellerListActivity}
 * in two-pane mode (on tablets) or a {@link SellerDetailActivity}
 * on handsets.
 */
public class SellerDetailFragment extends Fragment {
    private Seller current_seller;
    private View rootView;
    private DBHandler dbHandler;
    private int open_mode_flag;
    // UI stuff
    private TextView seller_name_input;
    private TextView seller_address_input;
    private TextView seller_city_input;
    private TextView GPSLat_input;
    private TextView GPSLon_input;
    private Button applySellerChanges_button;

    public SellerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("open_mode_flag")) {
            // Load values passed by ProductPriceDetailActivity
            open_mode_flag = getArguments().getInt("open_mode_flag");
            if (open_mode_flag != 2) {
                int seller_id = getArguments().getInt("current_seller_id");
                String seller_name = getArguments().getString("current_seller_name");
                String seller_address = getArguments().getString("current_seller_address");
                String seller_city = getArguments().getString("current_seller_city");
                current_seller = new Seller(seller_id, seller_name, seller_address, seller_city);
            }
            dbHandler = DBHandler.getInstance(this.getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manage_sellers_detail, container, false);
        // Initialize the UI
        seller_name_input = (TextView) rootView.findViewById(R.id.seller_name_input);
        seller_address_input = (TextView) rootView.findViewById(R.id.seller_address_input);
        seller_city_input = (TextView) rootView.findViewById(R.id.seller_city_input);
        GPSLat_input = (TextView) rootView.findViewById(R.id.GPSLat_input);
        GPSLon_input = (TextView) rootView.findViewById(R.id.GPSLon_input);
        applySellerChanges_button = (Button) rootView.findViewById(R.id.applySellerChanges_button);

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
            default:
                openReadOnly();
                break;
        }
        return rootView;
    }

    private void loadSellerValues() {
        seller_name_input.setText(current_seller.getSeller_name());
        seller_address_input.setText(current_seller.getAddress());
        seller_city_input.setText(current_seller.getCity());
    }

    private void triggerUI(boolean trigger) {
        // Trigger input fields
        seller_name_input.setEnabled(trigger);
        seller_address_input.setEnabled(trigger);
        seller_city_input.setEnabled(trigger);
//TODO:        GPSLat_input.setEnabled(trigger);
//TODO:        GPSLon_input.setEnabled(trigger);
        // Set focus
        seller_name_input.setFocusable(trigger);
        seller_address_input.setFocusable(trigger);
        seller_city_input.setFocusable(trigger);
//TODO:        GPSLat_input.setFocusable(trigger);
//TODO:        GPSLon_input.setFocusable(trigger);
        // Trigger the input button
        if (trigger)
            applySellerChanges_button.setVisibility(View.VISIBLE);
        else applySellerChanges_button.setVisibility(View.GONE);
    }

    private void openReadOnly() {
        if (current_seller != null) {
            // Load values in UI
            loadSellerValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_seller_info);
            // Disable UI
            triggerUI(false);
        }
    }

    private void openEditMode() {
        if (current_seller != null) {
            // Load values in UI
            loadSellerValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_seller_edit);
            // Enable UI
            triggerUI(true);
            seller_name_input.setEnabled(false);
            seller_name_input.setFocusable(false);

            applySellerChanges_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update current Seller, then update the DB
                    current_seller.setSeller_name(seller_name_input.getText().toString());
                    current_seller.setAddress(seller_address_input.getText().toString());
                    current_seller.setCity(seller_city_input.getText().toString());
                    // Update the DB
                    boolean updateResult = dbHandler.getSellerHandler().updateSeller(current_seller);
                    if (!updateResult) {
                        Snackbar.make(getView(), R.string.seller_duplicate_error, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        // Go back to the previous screen
                        NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), SellerListActivity.class));
                    }
                }
            });
        }
    }

    private void openAddMode() {
        // Set title on top
        this.getActivity().setTitle(R.string.title_seller_add);
        // Enable UI
        triggerUI(true);

        applySellerChanges_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update current Seller, then update the DB
                String seller_name = seller_name_input.getText().toString();
                String seller_address = seller_address_input.getText().toString();
                String seller_city = seller_city_input.getText().toString();
                current_seller = new Seller(seller_name, seller_address, seller_city);
                // Update the DB
                boolean updateResult = dbHandler.getSellerHandler().addSeller(current_seller);
                if (!updateResult) {
                    Snackbar.make(getView(), R.string.seller_duplicate_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    // Go back to the previous screen
                    NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), SellerListActivity.class));
                }
            }
        });
    }
}
