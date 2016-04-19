package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SQLiteManagerProvider implements SQLiteManagerDataProvider {

    public SQLiteManagerProvider(Context context) {
        this.context = context;
        database = (new DBHelper(context)).getWritableDatabase();
        loadImagesPool();
    }

    // SQLiteManagerDataProvider -------------------------------------------------------------------

    synchronized public void addNewsItemDTO(NewsDTO.Item item, String srcURL) {
        String[] strings = new String[]
                {
                        item.getDetailURL(),
                        srcURL,
                        item.getTitle(),
                        item.getImageURL(),
                        item.getDescription()
                };
        database.execSQL("insert or replace into news(url,src_url,title,image_url,description) values (?,?,?,?,?)", strings);
    }

    synchronized public void loadPage(NewsDTO newsDTO, String srcURL) {

        Cursor cursor =
                database.rawQuery("select url,title,image_url,description,src_url from news where src_url = ?",
                        new String[]{srcURL});

        if (cursor.moveToFirst()) {
            do {
                NewsDTO.Item item = new NewsDTO.Item();

                item.setDetailURL(cursor.getString(0));
                item.setTitle(cursor.getString(1));
                item.setImageURL(cursor.getString(2));
                item.setDescription(cursor.getString(3));

                newsDTO.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // INNER ---------------------------------------------------------------------------------------

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

            StringBuilder sb = new StringBuilder();
            sb.append("create table news (");
            sb.append("url text primary key,");
            sb.append("src_url text,");
            sb.append("title text not null,");
            sb.append("image_url text not null,");
            sb.append("description text not null");
            sb.append(")");

            database.execSQL(sb.toString());

            sb.setLength(0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("drop table if exists images");
            database.execSQL("drop table if exists news");
            database.execSQL("drop table if exists detailed_new");
            onCreate(database);
        }

        private static final String DATABASE_NAME = "db";
        private static final int DATABASE_VERSION = 11;
    }

    private SQLiteDatabase database;
    private Context context;
    private Map<String, Bitmap> imagesPool = new HashMap<>();
}
