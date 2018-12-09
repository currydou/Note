package com.yodoo.android.baseutil.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import com.yodoo.android.baseutil.keys.SP;
import com.yodoo.android.baseutil.sp.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.yodoo.android.baseutil.utils.Utils.getApp;

/**
 * Created by lib on 2017/7/12.
 */

public class StringUtil {
    /**
     * json 格式化
     *
     * @param json
     * @return
     */
    public static String jsonFormat(String json) {
        if (TextUtils.isEmpty(json)) {
            return "Empty/Null json content";
        }
        String message;
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }
        return message;
    }

    /**
     * xml 格式化
     *
     * @param xml
     * @return
     */
    public static String xmlFormat(String xml) {
        if (TextUtils.isEmpty(xml)) {
            return "Empty/Null xml content";
        }
        String message;
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            message = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (TransformerException e) {
            message = xml;
        }
        return message;
    }

    /**
     * 获取APP版本号
     *
     * @return
     */
    public static String getAppVersion() {
        PackageInfo pi = null;
        try {
            pi = getApp().getPackageManager().getPackageInfo(getApp().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pi != null) {
            return pi.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取语言
     *
     * @return
     */
    public static String getAppLanguage() {
        String spLanguage = SPUtil.getString(SP.MULTI_LANGUAGE);
        if (!TextUtils.isEmpty(spLanguage)) {
            return spLanguage;
        }
        Configuration config = getApp().getResources().getConfiguration();
        String language = "";
        if (Build.VERSION.SDK_INT >= 24) {
            language = config.getLocales().get(0).getLanguage();
        } else {
            language = config.locale.getLanguage();
        }
        return language;
    }
}
