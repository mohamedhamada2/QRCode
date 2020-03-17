package com.alatheer.abnaa.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.UserModel;
import com.google.gson.Gson;

public class MySharedPreferences2 {
    Context context;
    SharedPreferences mPrefs;
    private static MySharedPreferences2 instance = null;


    public MySharedPreferences2(Context context) {
        this.context = context;
    }

    public MySharedPreferences2() {

    }

    public static MySharedPreferences2 getInstance() {
        if (instance == null) {
            instance = new MySharedPreferences2();
        }
        return instance;
    }

    public void Create_Update_UserData(Context context, Charities_Model charities_model) {
        mPrefs = context.getSharedPreferences("user1", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(charities_model);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("user_data1", userData);
        editor.apply();
        Create_Update_Session(context, "login1");

    }

    public void Create_Update_Session(Context context, String session) {
        mPrefs = context.getSharedPreferences("session1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("state", session);
        editor.apply();
    }


    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session2", Context.MODE_PRIVATE);
        String session = preferences.getString("state", "logout1");
        return session;
    }


    public Charities_Model Get_UserData(Context context) {

        mPrefs = context.getSharedPreferences("user1", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = mPrefs.getString("user_data1", "");
        Charities_Model charities_model = gson.fromJson(userData, Charities_Model.class);
        return charities_model;


    }
    public Charities_Model Get_UserData2() {

        mPrefs = context.getSharedPreferences("user1", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = mPrefs.getString("user_data1", "");
        Charities_Model charities_model = gson.fromJson(userData, Charities_Model.class);
        return charities_model;


    }

    public void ClearData(Context context) {
        Charities_Model charities_model = null;
        mPrefs = context.getSharedPreferences("user1", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(charities_model);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("user_data1", userData);
        editor.apply();
        Create_Update_Session(context, "login1");
    }
}
