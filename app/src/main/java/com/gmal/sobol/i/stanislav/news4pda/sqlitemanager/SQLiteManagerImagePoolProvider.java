package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.graphics.Bitmap;

public interface SQLiteManagerImagePoolProvider {
    Bitmap getBitmapFromPoolByURL(String url);
    void setBitmapToPool(String url, Bitmap bitmap);
}
