package yithian.cosacompro.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import yithian.cosacompro.db.dbclasses.GList;
import yithian.cosacompro.db.dbhandlers.GListHandler;

public class SettingsManager {
    private Activity activity;
    private SharedPreferences sharedPref;
    private GList defaultGList;

    public SettingsManager() {
    }

    public SettingsManager(Activity activity) {
        this.activity = activity;
        sharedPref = activity.getSharedPreferences("CosaComproSettings", Context.MODE_PRIVATE);
        getDefaultList();
    }

    public String getDefaultListName() {
        return sharedPref.getString("defaultList_name", "### sample list ###");
    }

    public int getDefaultListID() {
        return sharedPref.getInt("defaultList_id", 1);
    }

    public void setDefaultList(GList defaultGList) {
        if (defaultGList != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("defaultList_id", defaultGList.getGList_id());
            editor.putString("defaultList_name", defaultGList.getGList_name());
            editor.apply();
        }
    }

    public GList getDefaultList() {
        GListHandler gListHandler = new GListHandler(activity, null, null, 1);
        defaultGList = gListHandler.getGListFromID(getDefaultListID());
        return defaultGList;
    }
}
