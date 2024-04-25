package com.motiv.motiv8.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MySharedPreferences {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_OBJECT = "loginObj";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveLoginObject(Context context, Object object) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(KEY_OBJECT, json);
        editor.apply();
    }

    public static <T> T getLoginObject(Context context, Class<T> classOfT) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(KEY_OBJECT, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, classOfT);
    }
}
