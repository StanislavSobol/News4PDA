package com.gmal.sobol.i.stanislav.news4pda;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gmal.sobol.i.stanislav.news4pda.data.DataProvider;
import com.gmal.sobol.i.stanislav.news4pda.data.DataProviderPresentable;
import com.gmal.sobol.i.stanislav.news4pda.data.parser.BaseParser;
import com.gmal.sobol.i.stanislav.news4pda.data.parser.New4PDAParser;
import com.gmal.sobol.i.stanislav.news4pda.di.DaggerRealComponents;
import com.gmal.sobol.i.stanislav.news4pda.di.RealComponents;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.Getter;

@Module
public class MApplication extends android.app.Application {

    @Getter
    private static MApplication instance;
    private RealComponents dagger2RealComponents;
    private boolean mainActivityIsAlive;


    public static boolean isOnlineWithToast(boolean showToastIfNoInet) {
        ConnectivityManager cm =
                (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean result = netInfo != null && netInfo.isConnectedOrConnecting();

        if (showToastIfNoInet && !result) {
            final String s = instance.getResources().getString(R.string.error_no_internet);
            Toast.makeText(instance, s, Toast.LENGTH_LONG).show();
        }

        return result;
    }

    public static RealComponents getDaggerComponents() {
        return instance.dagger2RealComponents;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dagger2RealComponents = DaggerRealComponents.builder().mApplication(this).build();
        instance = this;
    }


    @Provides
    @Singleton
    New4PDAParser providesNew4PDAParser() {
        return new BaseParser();
    }

    @Provides
    @Singleton
    DataProviderPresentable providesDataProviderPresentable() {
        return new DataProvider();
    }

    public void createComponents(boolean callFromMainActivity) {
        if (callFromMainActivity || !mainActivityIsAlive) {
            dagger2RealComponents = DaggerRealComponents.builder().mApplication(this).build();
//            sqlLiteQRMManager = new SQLORMManager();
        }

        if (callFromMainActivity) {
            mainActivityIsAlive = true;
        }
    }

    public void releaseComponents(boolean callFromMainActivity) {
        if (callFromMainActivity || !mainActivityIsAlive) {
            dagger2RealComponents = null;
//            if (sqlLiteQRMManager != null && sqlLiteQRMManager.isOpen()) {
//                sqlLiteQRMManager.close();
//            }
        }

        if (callFromMainActivity) {
            mainActivityIsAlive = false;
        }
    }


}
