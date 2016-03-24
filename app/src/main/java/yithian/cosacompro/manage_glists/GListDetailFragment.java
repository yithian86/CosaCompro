package yithian.cosacompro.manage_glists;

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
import yithian.cosacompro.db.dbclasses.GList;

/**
 * A fragment representing a single Seller detail screen.
 * This fragment is either contained in a {@link GListListActivity}
 * in two-pane mode (on tablets) or a {@link GListDetailActivity}
 * on handsets.
 */
public class GListDetailFragment extends Fragment {
    private GList current_gList;
    private View rootView;
    private DBHandler dbHandler;
    private int open_mode_flag;
    // UI stuff
    private TextView glist_name_input;
    private TextView glist_start_input;
    private TextView glist_end_input;
    private Button applyGListChanges_button;

    public GListDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("open_mode_flag")) {
            // Load values passed by ProductPriceDetailActivity
            open_mode_flag = getArguments().getInt("open_mode_flag");
            if (open_mode_flag != 2) {
                int glist_id = getArguments().getInt("current_glist_id");
                String glist_name = getArguments().getString("current_glist_name");
                String glist_start = getArguments().getString("current_glist_start");
                String glist_end = getArguments().getString("current_glist_end");
                current_gList = new GList(glist_id, glist_name, glist_start, glist_end);
            }
            dbHandler = DBHandler.getInstance(this.getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manage_glists_detail, container, false);
        // Initialize the UI
        glist_name_input = (TextView) rootView.findViewById(R.id.glist_name_input);
        glist_start_input = (TextView) rootView.findViewById(R.id.glist_start_input);
        glist_end_input = (TextView) rootView.findViewById(R.id.glist_end_input);
        applyGListChanges_button = (Button) rootView.findViewById(R.id.applyGListChanges_button);

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

    private void loadGListValues() {
        glist_name_input.setText(current_gList.getGList_name());
        glist_start_input.setText(current_gList.getGList_start());
        glist_end_input.setText(current_gList.getGList_end());
    }

    private void triggerUI(boolean trigger) {
        // Trigger input fields
        glist_name_input.setEnabled(trigger);
        glist_start_input.setEnabled(trigger);
        glist_end_input.setEnabled(trigger);
        // Set focus
        glist_name_input.setFocusable(trigger);
        glist_start_input.setFocusable(trigger);
        glist_end_input.setFocusable(trigger);
        // Trigger the input button
        if (trigger)
            applyGListChanges_button.setVisibility(View.VISIBLE);
        else applyGListChanges_button.setVisibility(View.GONE);
    }

    private void openReadOnly() {
        if (current_gList != null) {
            // Load values in UI
            loadGListValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_glist_info);
            // Disable UI
            triggerUI(false);
        }
    }

    private void openEditMode() {
        if (current_gList != null) {
            // Load values in UI
            loadGListValues();
            // Set title on top
            this.getActivity().setTitle(R.string.title_glist_edit);
            // Enable UI
            triggerUI(true);

            applyGListChanges_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update current Seller, then update the DB
                    current_gList.setGList_name(glist_name_input.getText().toString());
                    current_gList.setGList_start(glist_start_input.getText().toString());
                    current_gList.setGList_end(glist_end_input.getText().toString());
                    // Update the DB
                    boolean updateResult = dbHandler.getGListHandler().updateGList(current_gList);
                    if (!updateResult) {
                        Snackbar.make(getView(), R.string.glist_duplicate_error, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        // Go back to the previous screen
                        NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), GListListActivity.class));
                    }
                }
            });
        }
    }

    private void openAddMode() {
        // Set title on top
        this.getActivity().setTitle(R.string.title_glist_add);
        // Enable UI
        triggerUI(true);

        applyGListChanges_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update current Seller, then update the DB
                String glist_name = glist_name_input.getText().toString();
                // TODO: uncomment the following three lines of code and delete the fourth:
//                String glist_start = glist_start_input.getText().toString();
//                String glist_end = glist_end_input.getText().toString();
//                current_gList = new GList(glist_name, glist_start, glist_end);
                current_gList = new GList(glist_name, "", "");
                // Update the DB
                boolean updateResult = dbHandler.getGListHandler().addGList(current_gList);
                if (!updateResult) {
                    Snackbar.make(getView(), R.string.glist_duplicate_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    // Go back to the previous screen
                    NavUtils.navigateUpTo(getActivity(), new Intent(v.getContext(), GListListActivity.class));
                }
            }
        });
    }
}
