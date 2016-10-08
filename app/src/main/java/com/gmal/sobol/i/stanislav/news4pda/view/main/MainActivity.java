package com.gmal.sobol.i.stanislav.news4pda.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gmal.sobol.i.stanislav.news4pda.Logger;
import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.R;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.presenter.PresenterUser;
import com.gmal.sobol.i.stanislav.news4pda.presenter.main.MainActivityPresenter;
import com.gmal.sobol.i.stanislav.news4pda.presenter.main.MainActivityPresenterForActivity;
import com.gmal.sobol.i.stanislav.news4pda.view.BaseActivity;
import com.gmal.sobol.i.stanislav.news4pda.view.details.DetailedActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, PresenterUser<MainActivityPresenterForActivity> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fullProgressBar)
    ProgressBar fullProgressBar;
    @Bind(R.id.recyclerProgressBar)
    ProgressBar recyclerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MApplication.getInstance().createComponents();
        final boolean realStart = isRealStart();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGraphics();

        loadPage(1, !realStart);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            MApplication.getInstance().releaseComponents();
        }
        super.onDestroy();
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
        getCastedPresenter().loadNewDataPartIfNeeded(position);
    }

    void showDetailedNew(ItemDTO item) {
        final Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
        intent.putExtra("url", item.getDetailURL());
        startActivity(intent);
    }

    private void initGraphics() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setVisibility(View.GONE);

        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NewsListAdapter(this));
    }

    private void setTitleOnlineStatus() {
        String title = getResources().getString(R.string.app_name);
        if (MApplication.isOnlineWithToast(true)) {
            setTitle("[ONLINE] " + title);
        } else {
            setTitle("[OFFLINE] " + title);
        }
    }

    private void loadPage(final int number, boolean fromCache) {
        setTitleOnlineStatus();
        MApplication.isOnlineWithToast(true);
        getCastedPresenter().loadPage(number, fromCache);
    }

    @Override
    public MainActivityPresenterForActivity createPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    public MainActivityPresenterForActivity getCastedPresenter() {
        return (MainActivityPresenterForActivity) getPresenter();
    }

    @Override
    public void buildPage(List<ItemDTO> itemsDTO, boolean fromCache) {
        Logger.write("MainActivity::buildPage");
    }

    @Override
    public void addItem(ItemDTO itemDTO) {
        fullProgressBar.setVisibility(View.GONE);
        getRecyclerViewAdapter().addItem(itemDTO);
    }

    private NewsListAdapter getRecyclerViewAdapter() {
        return ((NewsListAdapter) recyclerView.getAdapter());
    }
}
