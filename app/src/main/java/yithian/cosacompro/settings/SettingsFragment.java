package yithian.cosacompro.settings;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

import yithian.cosacompro.R;
import yithian.cosacompro.db.dbclasses.GList;
import yithian.cosacompro.db.dbhandlers.GListHandler;

public class SettingsFragment extends PreferenceFragment {
    private ListPreference listPreference;
    private SettingsManager settingsManager;
    private String defaultListName;
    private ArrayList<GList> array_GLists;
    private ArrayList<String> array_GListNames;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        // Create an instance of SharedPreferences
        settingsManager = new SettingsManager(this.getActivity());
        defaultListName = settingsManager.getDefaultListName();

        // Generate the "default_list" ListPreference stuff
        generateDefaultListField();
    }

    public void generateDefaultListField() {
        listPreference = (ListPreference) findPreference("default_list");
        if (listPreference != null) {
            GListHandler gListHandler = GListHandler.getInstance(getActivity().getApplicationContext());
            array_GLists = gListHandler.getGLists();
            array_GListNames = new ArrayList<>();
            for(GList index_GList: array_GLists) {
                array_GListNames.add(index_GList.getGList_name());
            }

            CharSequence[] charLists = new CharSequence[array_GListNames.size()];
            for (int i = 0; i < array_GListNames.size(); i++) {
                charLists[i] = array_GListNames.get(i);
            }

            listPreference.setTitle(getString(R.string.default_list_title) + ": " + defaultListName);
            listPreference.setDefaultValue(defaultListName);
            listPreference.setEntries(charLists);
            listPreference.setEntryValues(charLists);

            Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // newValue is the value you choose
                    defaultListName = newValue.toString();
                    int index = array_GListNames.indexOf(defaultListName);
                    settingsManager.setDefaultList(array_GLists.get(index));
                    listPreference.setTitle(getString(R.string.default_list_title) + ": " + defaultListName);
                    return false;
                }
            };
            listPreference.setOnPreferenceChangeListener(listener);
        }
    }
}
