package com.gmal.sobol.i.stanislav.news4pda;

import android.util.Log;

public class Logger {

    private static final String COMMON_TAG = "MyLog";
    private static final String ERROR_TAG = "MyErrors";
    private static final String PUSH_TAG = "MyPush";

    public static void write(String msg) {
        Log.d(COMMON_TAG, msg);
    }

    public static void writeError(String msg) {
        Log.d(ERROR_TAG, msg);
    }

    public static void writePush(String msg) {
        Log.d(PUSH_TAG, msg);
    }
}
