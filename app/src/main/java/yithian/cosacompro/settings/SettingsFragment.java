package yithian.cosacompro.settings;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

import yithian.cosacompro.R;
import yithian.cosacompro.db.dbhandlers.ListHandler;

public class SettingsFragment extends PreferenceFragment {
    private ListHandler listHandler;
    private ListPreference listPreference;
    private SettingsManager settingsManager;
    private String defaultList;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        // Create an instance of SharedPreferences
        settingsManager = new SettingsManager(this.getActivity());
        defaultList = settingsManager.getDefaultList();

        // Generate the "default_list" ListPreference stuff
        generateDefaultListField();
    }

    public void generateDefaultListField() {
        listPreference = (ListPreference) findPreference("default_list");
        if (listPreference != null) {
            listHandler = new ListHandler(this.getActivity().getApplicationContext(), null, null, 1);
            ArrayList<String> arrayLists = listHandler.listToArrayList();

            CharSequence[] charLists = new CharSequence[arrayLists.size()];
            for (int i = 0; i < arrayLists.size(); i++) {
                charLists[i] = arrayLists.get(i);
            }

            listPreference.setTitle(getString(R.string.default_list_title) + ": " + defaultList);
            listPreference.setDefaultValue(defaultList);
            listPreference.setEntries(charLists);
            listPreference.setEntryValues(charLists);

            Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // newValue is the value you choose
                    defaultList = newValue.toString();
                    settingsManager.setDefaultList(defaultList);
                    listPreference.setTitle(getString(R.string.default_list_title) + ": " + defaultList);
//                    System.out.println("New default list: " + settingsManager.getDefaultList());
                    return false;
                }
            };
            listPreference.setOnPreferenceChangeListener(listener);
        }
    }
}
