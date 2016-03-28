package com.gmal.sobol.i.stanislav.news4pda;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.parser.DetailedNewDTO;

import java.util.List;

public class DetailedNewScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_new_scrolling);

        initGraphics();
        requestData();
    }

    private void requestData() {
        CallbackBundle callbackBundle = new CallbackBundle();

        callbackBundle.setResult(new Runnable() {
            @Override
            public void run() {
                fullProgressBar.setVisibility(View.GONE);
                scrolledContent.setVisibility(View.VISIBLE);
                setContent();
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

    private void setContent() {
        DetailedNewDTO detailedNewDTO = News4PDAApplication.getParser4PDA().getParsedDetailedNewData();

        setTitle("4PD - " + detailedNewDTO.getTitle());

        titleTextView.setText(detailedNewDTO.getTitle());
        descriptionTextView.setText(detailedNewDTO.getDescription());
        new DownloadImageTask(this, titleImageView, true, null).safeExecute(detailedNewDTO.getTitleImageURL());

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.containerLayout);

        List<DetailedNewDTO.ContentItem> items = detailedNewDTO.getContentItems();
        for (DetailedNewDTO.ContentItem item : items) {
            View view;

            if (item.isImage()) {
                view = getLayoutInflater().inflate(R.layout.detailed_new_image_content, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.detailedNewContentImageView);
                new DownloadImageTask(this, imageView, false, null).safeExecute(item.getContent());
            } else {
                view = getLayoutInflater().inflate(R.layout.detailed_new_text_content, null);
                TextView textView = (TextView) view.findViewById(R.id.detailedNewContentTextView);
                textView.setText(item.getContent());
            }

            containerLayout.addView(view);
        }
    }

    private void initGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fullProgressBar = (ProgressBar) findViewById(R.id.fullProgressBar);
        scrolledContent = findViewById(R.id.scrolledContent);
        scrolledContent.setVisibility(View.GONE);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        titleImageView = (ImageView) findViewById(R.id.titleImageView);
    }

    private ProgressBar fullProgressBar;
    private View scrolledContent;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView titleImageView;
}
