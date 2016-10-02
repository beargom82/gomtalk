package com.dwkim.android.gomtalk.model;

/**
 * Created by dwkim on 2015-06-21.
 */
public interface ModelCallback {
    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;
    public static final int RESULT_CANCEL = -1;
    public static final int ERRORCODE_NONE = -1;

    void onComplete(int resultCode, int errorCode, Object data);
}
