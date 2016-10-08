package com.gmal.sobol.i.stanislav.news4pda.view.details;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.R;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.presenter.PresenterUser;
import com.gmal.sobol.i.stanislav.news4pda.presenter.details.DetailedActivityPresenter;
import com.gmal.sobol.i.stanislav.news4pda.presenter.details.DetailedActivityPresenterForActivity;
import com.gmal.sobol.i.stanislav.news4pda.view.BaseActivity;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailedActivity extends BaseActivity implements DetailedView, PresenterUser<DetailedActivityPresenterForActivity> {

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
        final boolean realStart = isRealStart();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_new_scrolling);
        initGraphics();
        MApplication.isOnlineWithToast(true);
        getCastedPresenter().loadData(!realStart, getIntent().getStringExtra("url"));
    }

    private void initGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }

    @Override
    public DetailedActivityPresenterForActivity createPresenter() {
        return new DetailedActivityPresenter();
    }

    @Override
    public DetailedActivityPresenterForActivity getCastedPresenter() {
        return (DetailedActivityPresenterForActivity) getPresenter();
    }

    @Override
    public void buildPage(DetailsMainDTO data) {
        fullProgressBar.setVisibility(View.GONE);
        scrolledContent.setVisibility(View.VISIBLE);

        setTitle(data.getTitle());
        titleTextView.setText(data.getTitle());
        descriptionTextView.setText(data.getDescription());

        if (!data.getTitleImageURL().isEmpty()) {
            RequestCreator requestCreator = Picasso.with(this)
                    .load(data.getTitleImageURL())
                    .placeholder(R.drawable.ic_menu_gallery);
            if (!MApplication.isOnlineWithToast(false)) {
                requestCreator.networkPolicy(NetworkPolicy.OFFLINE);
            }
            requestCreator.into(titleImageView);
        }

        final LinearLayout containerLayout = (LinearLayout) findViewById(R.id.containerLayout);

        for (DetailsItemDTO item : data.getItems()) {
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
