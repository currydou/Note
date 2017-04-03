package com.curry.signapp.request;

import android.content.Context;
import android.text.TextUtils;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.curry.signapp.BaseApplication;
import com.curry.signapp.R;
import com.curry.signapp.util.NetUtil;
import com.curry.signapp.util.SignAppLog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangkai
 * @Description: OKHttp请求
 * create at 2015/11/12 16:06
 */
public class OKHttpRequest {
    private static Context context;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * @param context  上下文
     * @param url      请求地址
     * @param tag      请求的标记
     * @param listener 回调接口
     */
    public static void RequestGet(final Context context, String url, String tag, final RequestResultListener listener) {
        BaseApplication.getOKHttpClient().cancel(tag);
        if (!NetUtil.checkNet(context)) {
            new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_net));
            listener.onFailed();
            return;
        }
        final Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        deliveryResult(context, listener, request);
    }

    /**
     * @param context  上下文
     * @param url      请求地址
     * @param tag      请求标记
     * @param listener 回调接口
     */
    public static void RequestPost(final Context context, String url, String tag,
                                   final Map<String, String> maps, final RequestResultListener listener) {
        if (!TextUtils.isEmpty(tag)) {
            BaseApplication.getOKHttpClient().cancel(tag);
        }
        if (!NetUtil.checkNet(context)) {
            new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_net));
            if (listener != null) {
                listener.onFailed();
            }
            return;
        }
        Request request;
//        if (maps.size() == 0) {
//            request = buildPostRequest(url, null, tag);
//        } else {
            Param[] paramsArr = map2Params(maps);
            request = buildPostRequest(url, paramsArr, tag);
//        }
        deliveryResult(context, listener, request);
    }

    public static void RequestPost(final Context context, String url,
                                   final Map<String, String> maps, final RequestResultListener listener) {
        RequestPost(context, url, null, maps, listener);
    }

    /**
     * application/json形式的Post接口
     *
     * @param context  上下文
     * @param url      请求地址
     * @param tag      请求标记
     * @param json     请求数据
     * @param listener 回调
     */
    public static void postWithNoKey(Context context, String url, String tag, String json, RequestResultListener listener) {
        if (!TextUtils.isEmpty(tag)) {
            BaseApplication.getOKHttpClient().cancel(tag);
        }
        if (!NetUtil.checkNet(context)) {
            new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_net));
            if (listener != null) {
                listener.onFailed();
            }
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        deliveryResult(context, listener, request);
    }

    /**
     * 单图上传
     *
     * @param context  上下文
     * @param url      请求地址
     * @param tag      请求标记
     * @param map      post附带参数
     * @param fileName 文件名
     * @param file     上传的文件
     * @param listener 回调
     */
    public static void uploadFile(final Context context, String url, String tag, final HashMap<String, String> map, String fileName, File file, final RequestResultListener listener) {
        BaseApplication.getOKHttpClient().cancel(tag);
        if (!NetUtil.checkNet(context)) {
            new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_net));
            listener.onFailed();
            return;
        }
        List<File> files = new ArrayList<>();
        files.add(file);
        List<String> fileNames = new ArrayList<>();
        fileNames.add(fileName);
        Request request = buildMultipartFormRequest(url, files, fileNames, map2Params(map), tag);
        deliveryResult(context, listener, request);
    }

    /**
     * 多图上传
     *
     * @param context   上下文
     * @param url       请求地址
     * @param tag       请求标记
     * @param map       post附带参数
     * @param fileNames 文件名
     * @param files     上传的文件
     * @param listener  回调
     */
    public static void uploadFiles(final Context context, String url, String tag, final HashMap<String, String> map, List<String> fileNames, List<File> files, final RequestResultListener listener) {
        BaseApplication.getOKHttpClient().cancel(tag);
        if (!NetUtil.checkNet(context)) {
            new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_net));
            listener.onFailed();
            return;
        }
        Request request = buildMultipartFormRequest(url, files, fileNames, map2Params(map), tag);
        deliveryResult(context, listener, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     */
    public static void downloadFile(final String url, final String destFileDir, final String filename, final RequestResultListener listener) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = BaseApplication.getOKHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                listener.onFailed();
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, filename);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    listener.onSuccess(file.getAbsolutePath());
                } catch (IOException e) {
                    listener.onFailed();
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    private static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 构建post请求
     */
    private static Request buildPostRequest(String url, Param[] params, String tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .tag(tag)
                .post(requestBody)
                .build();
    }

    /**
     * 自定义params实体
     */
    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    /**
     * 处理请求
     */
    private static void deliveryResult(final Context context, final RequestResultListener listener, Request request) {
        BaseApplication.getOKHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                BaseApplication.getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        SignAppLog.e("IOException===" + e);
//                        new SVProgressHUD(context).showErrorWithStatus(context.getString(R.string.toast_error_server));

                        if (listener != null) {
                            listener.onFailed();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String jsonString = response.body().string();
                SignAppLog.print4k(jsonString);
                BaseApplication.getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onSuccess(jsonString);
                        }
                    }
                });
            }

        });
    }

    /**
     * 拼接POST请求参数
     */
    private static Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static Request buildMultipartFormRequest(String url, List<File> files,
                                                     List<String> fileKeys, Param[] params, String tag) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            int size = files.size();
            for (int i = 0; i < size; i++) {
                File file = files.get(i);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys.get(i) + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .tag(tag)
                .post(requestBody)
                .build();
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private static Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

}
