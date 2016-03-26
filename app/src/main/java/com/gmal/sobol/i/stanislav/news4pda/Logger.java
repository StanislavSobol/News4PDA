package com.gmal.sobol.i.stanislav.news4pda;

import android.util.Log;

public class Logger {

    public static void write(String msg) {
        Log.d(COMMON_TAG, msg);
    }

    public static void writeVKManagerLog(String msg) {
        Log.d(VK_MANAGER_TAG, msg);
    }

    public static void writePush(String msg) {
        Log.d(PUSH_TAG, msg);
    }

    private static final String COMMON_TAG = "MyLog";
    private static final String VK_MANAGER_TAG = "MyVKManager";
    private static final String PUSH_TAG = "MyPush";
}
