package com.projectbelajar.yuukbelajar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private Context context;
    /** Pendlakarasian Shared Preferences yang berdasarkan paramater context */

    public Preferences(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setValues(String key, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(key, username);
        editor.apply();
    }
    public void setBoolValue(String key, boolean value){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getValues(String key){
        return getSharedPreference(context).getString(key, "");
    }

    public boolean getBoolValue(String key){
        return getSharedPreference(context).getBoolean(key, false);
    }

    public void logout() {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.clear();
        editor.apply();
    }

}
