package com.gmal.sobol.i.stanislav.news4pda.view.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;
import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.R;
import com.gmal.sobol.i.stanislav.news4pda.parser.DetailedNewDTO_old;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailedNewScrollingActivity extends AppCompatActivity {

    @Bind(R.id.fullProgressBar)
    ProgressBar fullProgressBar;
    @Bind(R.id.scrolledContent)
    View scrolledContent;
    @Bind(R.id.titleTextView)
    TextView titleTextView;
    @Bind(R.id.descriptionTextView)
    TextView descriptionTextView;
    @Bind(R.id.titleImageView)
    ImageView titleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_new_scrolling);

        initGraphics();
        requestData();
    }

    private void initGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
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
        MApplication.getParser4PDA().parseDetailedNew(url, callbackBundle);
    }

    private void setContent() {
        DetailedNewDTO_old detailedNewDTOOld = MApplication.getParser4PDA().getParsedDetailedNewData();

        if (detailedNewDTOOld.getTitle().isEmpty()) {
            setTitle("Doesn't work offline yet, sorry :(");
        } else {
            setTitle("4PDA - " + detailedNewDTOOld.getTitle());
        }

        titleTextView.setText(detailedNewDTOOld.getTitle());
        descriptionTextView.setText(detailedNewDTOOld.getDescription());

        if (!detailedNewDTOOld.getTitleImageURL().isEmpty()) {
            RequestCreator requestCreator = Picasso.with(this)
                    .load(detailedNewDTOOld.getTitleImageURL())
                    .placeholder(R.drawable.ic_menu_gallery);
            if (!MApplication.isOnlineWithToast(false)) {
                requestCreator.networkPolicy(NetworkPolicy.OFFLINE);
            }
            requestCreator.into(titleImageView);
        }

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.containerLayout);

        List<DetailedNewDTO_old.ContentItem_old> items = detailedNewDTOOld.getContentItemOlds();
        for (DetailedNewDTO_old.ContentItem_old item : items) {
            View view;

            if (item.isImage()) {
                view = getLayoutInflater().inflate(R.layout.detailed_new_image_content, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.detailedNewContentImageView);

                if (!item.getContent().isEmpty()) {
                    Picasso.with(this)
                            .load(item.getContent())
                            .placeholder(R.drawable.ic_menu_gallery)
                            .into(imageView);
                }
            } else {
                view = getLayoutInflater().inflate(R.layout.detailed_new_text_content, null);
                TextView textView = (TextView) view.findViewById(R.id.detailedNewContentTextView);
                textView.setText(item.getContent());
            }

            containerLayout.addView(view);
        }
    }
}
