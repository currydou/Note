package com.yodoo.android.baseutil.keys;

import android.support.annotation.StringDef;

/**
 * Created by lib on 2017/7/12.
 * SharedPreferences key
 */

public interface SP {
    String TOKEN = "TOKEN";
    String LEGALUNITID = "LEGALUNITID";
    String UID = "UID";
    String USER_INFO = "USER_INFO";
    String FIRST_ENTRY = "FIRST_ENTRY";
    String ACTIVE_UID = "ACTIVE_UID";
    String USER_BASE_URL = "USER_BASE_URL";
    String USER_PUSH_INVOICE = "USER_PUSH_INVOICE";
    String USER_DEVICE_TOKEN = "USER_DEVICE_TOKEN";
    String P_USER_LOGIN = "p_user_login";
    String IS_MAIN_OUT = "isMainOut";
    String DEVICE_TOKEN = "DEVICE_TOKEN";
    String USER_HAVE_QR = "USER_HAVE_QR";

    /**
     * 最后一次定位信息
     */
    String LAST_LOCATION = "last_location";
    /**
     * 最后一次登录的公司和用户
     */
    String LAST_COMPANY_ID = "LAST_COMPANY_ID";
    String LAST_EMPLOYEE = "LAST_EMPLOYEE";

    /*
    上一次输入的账号和密码
     */
    String USER_CELL_PHONE = "USER_CELL_PHONE";
    String USER_PASSWORD = "USER_PASSWORD";

    /*
    多语言切换保存
     */
    String MULTI_LANGUAGE = "MULTI_LANGUAGE";

    /*
    推送开关
     */
    String P_PUSH = "p_push_setting";
}
