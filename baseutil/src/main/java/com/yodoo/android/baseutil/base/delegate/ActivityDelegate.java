package com.yodoo.android.baseutil.base.delegate;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by lib on 2017/7/25.
 */

public interface ActivityDelegate extends Parcelable {
    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
