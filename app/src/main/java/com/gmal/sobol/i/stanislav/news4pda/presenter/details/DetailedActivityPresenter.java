package com.gmal.sobol.i.stanislav.news4pda.presenter.details;

import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.data.DataProviderPresentable;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.presenter.BasePresenter;
import com.gmal.sobol.i.stanislav.news4pda.view.details.DetailedView;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VZ on 05.10.2016.
 */
public class DetailedActivityPresenter extends BasePresenter implements DetailedActivityPresenterForActivity {

    @Inject
    DataProviderPresentable dataProviderPresentable;

    private DetailsMainDTO data;

    @Override
    protected void dagger2Inject() {
        MApplication.getDaggerComponents().inject(this);
    }

    @Override
    protected DetailedView getCastedView() {
        return (DetailedView) getView();
    }

    @Override
    public void loadData(boolean fromCache, final String url) {
        if (fromCache) {
            getCastedView().buildPage(data);
        } else {
            final Observable<DetailsMainDTO> observable = Observable.create(new Observable.OnSubscribe<DetailsMainDTO>() {
                @Override
                public void call(Subscriber<? super DetailsMainDTO> subscriber) {
                    data = dataProviderPresentable.getDetailedData(MApplication.isOnlineWithToast(false), url, subscriber);
                }
            });

            subscription = observable
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DetailsMainDTO>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(DetailsMainDTO detailsMainDTO) {
                            getCastedView().buildPage(detailsMainDTO);
                            dataProviderPresentable.writeDetailsMainDTO(detailsMainDTO);
                        }
                    });


        }

    }
}
