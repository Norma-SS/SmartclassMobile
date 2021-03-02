package com.projectbelajar.yuukbelajar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by AJISETYA on 12/5/2016.
 */

public class SessionManager {

    SharedPreferences pref;
    int mode = 0;
    //public string name;
    SharedPreferences.Editor editor;
    Context context;
    private Preferences preferences;

    public static final String pref_name = "crudpref";
    public static final String is_login = "islogin";
    public static final String password = "password";
    //public static final String EMP_ID = "emp_id";
    public static final String name = "name";
    public static final String EML = "eml";

    public static final String LEVEL = "userLevel";

    public SessionManager(Context context) {
        this.context = context;
        preferences = new Preferences(context);
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession(String email) {
        editor.putBoolean(is_login, true);
        editor.putString(password, email);
        //editor.putString(EML, email);
        //Toast.makeText(context, "isinya putstring " +password+ " ", Toast.LENGTH_LONG).show();
        editor.commit();
        preferences.setBoolValue("isLogin", true);
    }

    public void checkLogin() {
        if (!this.is_login()) {
            //Toast.makeText(context, "Belum Login....", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
//            Toast.makeText(context, "PLEASE LOGIN ", Toast.LENGTH_SHORT).show();
        } else if (preferences.getValues("level").equals("DOKTER")) {
            //Toast.makeText(context, "welcome to kis24 " +getUserDetails()+ " ", Toast.LENGTH_LONG).show();
            //getUserDetails();
            Intent i = new Intent(context, TabbedDoktor.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
        } else if (preferences.getValues("level").equals("KS")) {
            Intent i = new Intent(context, AboutActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
        } else if (preferences.getValues("level").equals("GURU") || preferences.getValues("level").equals("WALIKELAS")) {
            Intent i = new Intent(context, TabbedGuru.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
        } else {
            Intent i = new Intent(context, Tabbed.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }

    public static boolean is_login() {
        return false;
    }

    public void logout() {
        preferences.setBoolValue("isLogin", false);
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(pref_name, pref.getString(pref_name, null));
        user.put(password, pref.getString(password, null));
        String eml = pref.getString(password, null);
        //Toast.makeText(context, "ini email session nya !!! : " +eml+ " ", Toast.LENGTH_LONG).show();
        return user;
    }

}