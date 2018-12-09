package com.yodoo.android.baseutil.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.yodoo.android.baseutil.keys.SP;
import com.yodoo.android.baseutil.sp.SPUtil;

import java.util.Locale;

public class LanguageUtil {

    public static final String EN = "en", CN = "zh", DEFAULT = "default";
    private static Activity mActivity;

    // 只是记录 APP中选择的语言状态，做标记
    public static String getLanguage() {
        String language = SPUtil.getString(SP.MULTI_LANGUAGE);
        if (TextUtils.isEmpty(language)) {
            SPUtil.setString(SP.MULTI_LANGUAGE, DEFAULT);
            language = DEFAULT;
        }
        return language;
    }

    //为了适应 后台返回的参数设置的修改
    public static String getLanguageParam() {
        String language = getLanguage();
        if (TextUtils.equals(language, DEFAULT)) {
            if (TextUtils.equals(Locale.getDefault().getLanguage(), Locale.US.getLanguage())) {
                language = EN;
            } else {
                language = CN;
            }
        }
        return language;
    }

    public static String getNoLanguage() {
        String language = getLanguage();
        if (TextUtils.equals(language, DEFAULT)) {
            if (TextUtils.equals(Locale.getDefault().getLanguage(), Locale.US.getLanguage()) ||
                    TextUtils.equals(Locale.getDefault().getLanguage(), Locale.SIMPLIFIED_CHINESE.getLanguage())) {
                language = CN;
            }
        }
        return language;
    }

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static void setLanguage() {
        if (Utils.language) {
            String language = SPUtil.getString(SP.MULTI_LANGUAGE);
            Resources resources = Utils.getApp().getResources();
            Configuration config1 = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Locale locale;
            if (language.equals(LanguageUtil.CN)) {
                locale=Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals(LanguageUtil.EN)) {
                locale=Locale.US;
            } else {
                locale=Locale.getDefault();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config1.setLocale(locale);
            } else {
                config1.locale = locale;
            }
            saveLanguage(locale);
            resources.updateConfiguration(config1, dm);
        } else {
            Resources resources = mActivity.getResources();
            Configuration config1 = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config1.setLocale(Locale.SIMPLIFIED_CHINESE);
            } else {
                config1.locale = Locale.SIMPLIFIED_CHINESE;
            }
            resources.updateConfiguration(config1, dm);
        }
    }

    private static void saveLanguage(Locale locale) {
        if (locale == null) {
            return;
        }
        if (locale.getLanguage().contains(LanguageUtil.EN)) {
            SPUtil.setString(SP.MULTI_LANGUAGE, LanguageUtil.EN);
        }else {
            SPUtil.setString(SP.MULTI_LANGUAGE, LanguageUtil.CN);
        }
    }

}
