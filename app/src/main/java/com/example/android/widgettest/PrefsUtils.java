package com.example.android.widgettest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android on 10.06.2016.
 */
public class PrefsUtils {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Const.PREFS_TAG, Context.MODE_PRIVATE);
    }

    public static void saveUrl(String url, Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(Const.URL_TAG, url);
        editor.commit();
    }

    public static String getUrl(Context context) {
        return getPrefs(context).getString(Const.URL_TAG, "");
    }
}
