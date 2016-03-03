package yithian.cosacompro.managesellers;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import yithian.cosacompro.R;
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
    private int open_mode_flag;
    // UI stuff
    private TextView seller_name_input;
    private TextView seller_address_input;
    private TextView seller_city_input;
    private TextView GPSLat_input;
    private TextView GPSLon_input;
    private Button applySellerChanges_button;
    private CollapsingToolbarLayout appBarLayout;

    public SellerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("current_seller_name")) {
            // Load values passed by SellerDetailActivity
            open_mode_flag = getArguments().getInt("open_mode_flag");
            int seller_id = getArguments().getInt("current_seller_id");
            String seller_name = getArguments().getString("current_seller_name");
            String seller_address = getArguments().getString("current_seller_address");
            String seller_city = getArguments().getString("current_seller_city");

            current_seller = new Seller(seller_id, seller_name, seller_address, seller_city);
            Log.d("current_seller", current_seller.getSeller_name());

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.seller_detail, container, false);
        // Initialize the UI
        seller_name_input = (TextView) rootView.findViewById(R.id.seller_name_input);
        seller_address_input = (TextView) rootView.findViewById(R.id.seller_address_input);
        seller_city_input = (TextView) rootView.findViewById(R.id.seller_city_input);
        GPSLat_input = (TextView) rootView.findViewById(R.id.GPSLat_input);
        GPSLon_input = (TextView) rootView.findViewById(R.id.GPSLon_input);
        applySellerChanges_button = (Button) rootView.findViewById(R.id.applySellerChanges_button);

        // Show the dummy content as text in a TextView.
        if (current_seller != null) {
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
            }
        }
        return rootView;
    }

    private void loadSellerValues() {
        seller_name_input.setText(current_seller.getSeller_name());
        seller_address_input.setText(current_seller.getAddress());
        seller_city_input.setText(current_seller.getCity());
    }

    private void openReadOnly() {
        // Load values
        loadSellerValues();
        // Set title on top
        if (appBarLayout != null) {
            appBarLayout.setTitle("Info punto vendita");
        }
        // Disable input
        seller_name_input.setEnabled(false);
        seller_address_input.setEnabled(false);
        seller_city_input.setEnabled(false);
        GPSLat_input.setEnabled(false);
        GPSLon_input.setEnabled(false);
        // Disable focus
        seller_name_input.setFocusable(false);
        seller_address_input.setFocusable(false);
        seller_city_input.setFocusable(false);
        GPSLat_input.setFocusable(false);
        GPSLon_input.setFocusable(false);
        // Hide the input button
        applySellerChanges_button.setVisibility(View.GONE);
    }

    private void openEditMode() {
        // Load values
        loadSellerValues();
        // Set title on top
        if (appBarLayout != null) {
            appBarLayout.setTitle("Modifica punto vendita");
        }
        // Enable input
        seller_name_input.setEnabled(true);
        seller_address_input.setEnabled(true);
        seller_city_input.setEnabled(true);
        GPSLat_input.setEnabled(true);
        GPSLon_input.setEnabled(true);
        // Set focus
        seller_address_input.setFocusable(true);
        seller_city_input.setFocusable(true);
        GPSLat_input.setFocusable(true);
        GPSLon_input.setFocusable(true);
        seller_name_input.setFocusable(true);
        // Show the input button
        applySellerChanges_button.setVisibility(View.VISIBLE);
    }

    private void openAddMode() {
        // Set title on top
        if (appBarLayout != null) {
            appBarLayout.setTitle("Aggiungi punto vendita");
        }
        // Enable input
        seller_name_input.setEnabled(true);
        seller_address_input.setEnabled(true);
        seller_city_input.setEnabled(true);
        GPSLat_input.setEnabled(true);
        GPSLon_input.setEnabled(true);
        // Set focus
        seller_address_input.setFocusable(true);
        seller_city_input.setFocusable(true);
        GPSLat_input.setFocusable(true);
        GPSLon_input.setFocusable(true);
        seller_name_input.setFocusable(true);
        // Show the input button
        applySellerChanges_button.setVisibility(View.VISIBLE);
    }
}
