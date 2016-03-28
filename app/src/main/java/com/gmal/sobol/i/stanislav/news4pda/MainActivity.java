package com.gmal.sobol.i.stanislav.news4pda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDAViewable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGraphics();
        parser4PDA.clearData();
        loadPage(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitleOnlineStatus();
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void checkForNextPage(int position) {
        if (position + 1 >= parser4PDA.getParsedNewsData().size()) {
            loadPage(currentPageNumber + 1);

        }
    }

    void showDetailedNew(NewsDTO.Item item) {
        Intent intent = new Intent(MainActivity.this, DetailedNewScrollingActivity.class);
        intent.putExtra("url", item.getDetailURL());
        startActivity(intent);
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

    private void setTitleOnlineStatus() {
        String title = getResources().getString(R.string.app_name);
        if (News4PDAApplication.isOnlineWithToast(true)) {
            setTitle("[ONLINE] " + title);
        } else {
            setTitle("[OFFLINE] " + title);
        }
    }

    private void loadPage(final int number) {
        setTitleOnlineStatus();

        if (number > 1) {
            recyclerProgressBar.setVisibility(View.VISIBLE);
        }

        CallbackBundle callbackBundle = new CallbackBundle();

        callbackBundle.setResult(new Runnable() {
            @Override
            public void run() {
                boolean added;
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(new NewsListAdapter(parser4PDA.getParsedNewsData(), MainActivity.this));
                    added = true;
                } else {
                    added =
                            ((NewsListAdapter) recyclerView.getAdapter()).addNews(parser4PDA.getParsedNewsData());
                }
                fullProgressBar.setVisibility(View.GONE);
                recyclerProgressBar.setVisibility(View.GONE);
                if (added) {
                    currentPageNumber = number;
                }
            }
        });

        callbackBundle.setError(new Runnable() {
            @Override
            public void run() {
                // TODO notify about the error
            }
        });

        parser4PDA.parseNewsPage(number, callbackBundle);
    }

    private Parser4PDAViewable parser4PDA = News4PDAApplication.getParser4PDA();
    private RecyclerView recyclerView;
    private ProgressBar fullProgressBar;
    private ProgressBar recyclerProgressBar;
    private int currentPageNumber = 0; // begins from 1
}
