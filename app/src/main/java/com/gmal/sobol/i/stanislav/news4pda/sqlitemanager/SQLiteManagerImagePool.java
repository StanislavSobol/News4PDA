package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.graphics.Bitmap;

public interface SQLiteManagerImagePool {
    Bitmap getBitmapFromPoolByURL(String url);
    void setBitmapToPool(String url, Bitmap bitmap);
}
