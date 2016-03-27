package com.gmal.sobol.i.stanislav.news4pda;

import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDAViewable;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManager;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManagerViewable;

public class News4PDAApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.write("News4PDAApplication");
        sqLiteManagerViewable = new SQLiteManager(this);
    }

    public static Parser4PDAViewable getParser4PDA() {
        return parser4PDA;
    }

    public static SQLiteManagerViewable getSqLiteManagerViewable() {
        return sqLiteManagerViewable;
    }

    private static Parser4PDAViewable parser4PDA = new Parser4PDA();
    private static SQLiteManagerViewable sqLiteManagerViewable;
}
