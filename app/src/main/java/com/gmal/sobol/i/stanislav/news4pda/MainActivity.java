package com.gmal.sobol.i.stanislav.news4pda;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDAViewable;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManager;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManagerViewable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        initGraphics();

        sqLiteManagerViewable = new SQLiteManager(this);

        parser4PDA.clearData();
        loadPage(1);
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fullProgressBar = (ProgressBar) findViewById(R.id.fullProgressBar);
        recyclerProgressBar = (ProgressBar) findViewById(R.id.recyclerProgressBar);
    }

    private void loadPage(final int number) {
        if (number > 1) {
            recyclerProgressBar.setVisibility(View.VISIBLE);
        }

        CallbackBundle callbackBundle = new CallbackBundle();

        callbackBundle.setResult(new Runnable() {
            @Override
            public void run() {
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(new NewsListAdapter(parser4PDA.getParsedData()));
                } else {
                    ((NewsListAdapter)recyclerView.getAdapter()).addNews(parser4PDA.getParsedData());
                }
                fullProgressBar.setVisibility(View.GONE);
                recyclerProgressBar.setVisibility(View.GONE);
                currentPageNumber = number;
            }
        });

        callbackBundle.setError(new Runnable() {
            @Override
            public void run() {
                // TODO notify about the error
            }
        });

        parser4PDA.parsePage(number, callbackBundle);
    }

    public void checkForNextPage(int position) {
        if (position + 1 >= parser4PDA.getParsedData().size()) {
            loadPage(++currentPageNumber);
            Logger.write("page currentPageNumber");
        }
    }

    SQLiteManagerViewable sqLiteManagerViewable;

    private static MainActivity instance;

    private Parser4PDAViewable parser4PDA = new Parser4PDA();
    private RecyclerView recyclerView;
    private ProgressBar fullProgressBar;
    private ProgressBar recyclerProgressBar;
    private int currentPageNumber = 0; // begining from 1
}
