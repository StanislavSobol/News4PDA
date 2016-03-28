package com.gmal.sobol.i.stanislav.news4pda;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDAViewable;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManager;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManagerImagePool;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManagerDataPool;

public class News4PDAApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        isOnlineWithToast(true);
        sqLiteManager = new SQLiteManager(this);
        parser4PDA = new Parser4PDA();
    }

    public static Parser4PDAViewable getParser4PDA() {
        return parser4PDA;
    }

    public static SQLiteManagerImagePool getSqLiteManagerImagePool() {
        return sqLiteManager;
    }

    public static SQLiteManagerDataPool getSqLiteManagerWriteable() {
        return sqLiteManager;
    }

    public static boolean isOnlineWithToast(boolean showToastIfNoInet) {
        ConnectivityManager cm =
                (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean result = netInfo != null && netInfo.isConnectedOrConnecting();

        if (showToastIfNoInet && !result) {
            String s = instance.getResources().getString(R.string.error_no_internet);
            Toast.makeText(instance, s, Toast.LENGTH_LONG).show();
        }

        return result;
    }

    private static News4PDAApplication instance;
    private static Parser4PDAViewable parser4PDA;
    private static SQLiteManager sqLiteManager;
}
