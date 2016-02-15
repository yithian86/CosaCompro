package yithian.cosacompro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private SharedPreferences sharedPref;

    public SettingsManager() {
    }

    public SettingsManager(Activity activity) {
        sharedPref = activity.getSharedPreferences("CosaComproSettings", Context.MODE_PRIVATE);
    }

    public String getDefaultList() {
        String defaultList = sharedPref.getString("defaultList", "ops opis");
        return defaultList;
    }

    public void setDefaultList(String defaultList) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("defaultList", defaultList);
        editor.commit();
    }
}
