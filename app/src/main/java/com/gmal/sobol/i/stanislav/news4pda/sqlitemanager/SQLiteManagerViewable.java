package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.graphics.Bitmap;

public interface SQLiteManagerViewable {
    Bitmap getBitmapFromPoolByURL(String url);
    void setBitmapToPool(String url, Bitmap bitmap);
}
