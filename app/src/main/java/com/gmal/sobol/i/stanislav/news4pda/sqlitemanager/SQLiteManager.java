package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SQLiteManager implements SQLiteManagerViewable {

    @Override
    public Bitmap getBitmapFromPoolByURL(String url) {
        return imagesPool.get(url);
    }

    @Override
    synchronized public void setBitmapToPool(String url, Bitmap bitmap) {
        if (imagesPool.get(url) == null) {
            imagesPool.put(url, bitmap);

            database.execSQL("insert into images(url) values (?)", new String[]{url});
            Cursor cursor = database.rawQuery("select last_insert_rowid()", null);
            cursor.moveToFirst();
            String filename = getImagesPath() + cursor.getInt(0) + ".jpg";
            cursor.close();

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public SQLiteManager(Context context) {
        this.context = context;
        database = (new DBHelper(context)).getWritableDatabase();
        loadImagesPool();
    }

    private String getImagesPath() {
        return context.getFilesDir().toString() + "/";
    }

    private void loadImagesPool() {
        Cursor cursor = database.rawQuery("select _id, url from images", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);

                try {
                    File imgFile = new File(getImagesPath() + id + ".jpg");
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        String url = cursor.getString(1);
                        imagesPool.put(url, bitmap);
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL("create table images ( _id integer primary key, url text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("drop table if exists images");
            onCreate(database);
        }

        private static final String DATABASE_NAME = "db";
        private static final int DATABASE_VERSION = 1;
    }

    private SQLiteDatabase database;
    private Context context;
    private Map<String, Bitmap> imagesPool = new HashMap<>();
}
