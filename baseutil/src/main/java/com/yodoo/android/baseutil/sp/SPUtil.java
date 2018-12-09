package com.yodoo.android.baseutil.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.yodoo.android.baseutil.keys.SP;

/**
 * Created by lib on 2017/7/12.
 */

public class SPUtil {
    private static final String P_NAME = "preferences";
    private static SharedPreferences preferences;

    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(P_NAME, 0);
        }
    }

    public static String getString(String key) {
        return preferences.getString(key, "");
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor e = preferences.edit().putString(key, value);
        e.commit();
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor e = preferences.edit().putBoolean(key, value);
        e.commit();
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor e = preferences.edit().putInt(key, value);
        e.commit();
    }

    public static boolean isFirstEntry() {
        return getBoolean(SP.FIRST_ENTRY, true);
    }

    public static void setFristEntry() {
        setBoolean(SP.FIRST_ENTRY, false);
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getUserToken());
    }

    public static void setUserToken(String userToken) {
        if (TextUtils.isEmpty(userToken)) setString(SP.TOKEN, "");
        else setString(SP.TOKEN, userToken);
    }

    public static void setLegalUnitID(String legalUnitID) {
        if (TextUtils.isEmpty(legalUnitID)) setString(SP.LEGALUNITID, "");
        else setString(SP.LEGALUNITID, legalUnitID);
    }

    public static String getUserToken() {
        return getString(SP.TOKEN);
    }

    public static String getLegalUnitID() {
        return getString(SP.LEGALUNITID);
    }

    public static void setUserCellPhone(String userCellPhone) {
        if (TextUtils.isEmpty(userCellPhone)) {
            setString(SP.USER_CELL_PHONE, "");
        } else {
            String cellPhoneBase64 = new String(Base64.encode(userCellPhone.getBytes(), Base64.DEFAULT));
            setString(SP.USER_CELL_PHONE, cellPhoneBase64);
        }
    }

    public static String getUserCellPhone() {
        String getSaveCellPhone = getString(SP.USER_CELL_PHONE);
        String getUserPhone =  new String(Base64.decode(getSaveCellPhone.getBytes(), Base64.DEFAULT));
        return getUserPhone;
    }

    public static void setUserPassword(String userPassword) {
        if (TextUtils.isEmpty(userPassword)) {
            setString(SP.USER_PASSWORD, "");
        } else {
            String passwordBase64 = new String(Base64.encode(userPassword.getBytes(), Base64.DEFAULT));
            setString(SP.USER_PASSWORD, passwordBase64);
        }
    }

    public static String getUserPassword() {
        String getSavePassword = getString(SP.USER_PASSWORD);
        String getUserPassWord =  new String(Base64.decode(getSavePassword.getBytes(), Base64.DEFAULT));
        return getUserPassWord;
    }

}
