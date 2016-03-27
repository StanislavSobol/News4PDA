package com.gmal.sobol.i.stanislav.news4pda;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

public class DetailedNewScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_new_scrolling);

        initGraphics();

        CallbackBundle callbackBundle = new CallbackBundle();

        callbackBundle.setResult(new Runnable() {
            @Override
            public void run() {
                fullProgressBar.setVisibility(View.GONE);
                scrolledContent.setVisibility(View.VISIBLE);
            }
        });

        callbackBundle.setError(new Runnable() {
            @Override
            public void run() {
                // TODO notify about the error
            }
        });

        String url = getIntent().getStringExtra("url");
        News4PDAApplication.getParser4PDA().parseDetailedNew(url, callbackBundle);
    }

    private void initGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fullProgressBar = (ProgressBar) findViewById(R.id.fullProgressBar);
        scrolledContent = findViewById(R.id.scrolledContent);
        scrolledContent.setVisibility(View.GONE);
    }

    private ProgressBar fullProgressBar;
    private View scrolledContent;
}
