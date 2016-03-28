package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gmal.sobol.i.stanislav.news4pda.Logger;
import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SQLiteManager implements SQLiteManagerImagePool, SQLiteManagerDataPool {

    public SQLiteManager(Context context) {
        this.context = context;
        database = (new DBHelper(context)).getWritableDatabase();
        loadImagesPool();
    }

    // SQLiteManagerImagePool ----------------------------------------------------------------------

    public Bitmap getBitmapFromPoolByURL(String url) {
        return imagesPool.get(url);
    }

    synchronized public void setBitmapToPool(String url, Bitmap bitmap) {
        if (imagesPool.get(url) == null && bitmap != null) {
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

    // SQLiteManagerWriteable ----------------------------------------------------------------------

    private static int numInserted = 0;

    synchronized public void addNewsItemDTO(NewsDTO.Item item, String srcURL) {
        String[] strings = new String[]
                {
                        item.getDetailURL(),
                        srcURL,
                        item.getTitle(),
                        item.getDetailURL(),
                        item.getDescription()
                };
        database.execSQL("insert or replace into news(url,src_url,title,image_url,description) values (?,?,?,?,?)", strings);

        Logger.write(Integer.valueOf(++numInserted).toString());
    }

    synchronized public void loadPage(NewsDTO newsDTO, String srcURL) {

        Cursor cursor =
                database.rawQuery("select url,title,image_url,description,src_url from news where src_url = ?",
                        new String[]{srcURL});

        Logger.write("srcURL = " + srcURL);
        Logger.write("getCount() = " + Integer.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                Logger.write(cursor.getString(4));

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
        private static final int DATABASE_VERSION = 10;
    }

    private SQLiteDatabase database;
    private Context context;
    private Map<String, Bitmap> imagesPool = new HashMap<>();
}
