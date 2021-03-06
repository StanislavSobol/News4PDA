package com.gmal.sobol.i.stanislav.news4pda.presenter.main;

import com.gmal.sobol.i.stanislav.news4pda.Logger;
import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.data.DataProviderPresentable;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.presenter.BasePresenter;
import com.gmal.sobol.i.stanislav.news4pda.view.main.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VZ on 16.08.2016.
 */
public class MainActivityPresenter extends BasePresenter implements MainActivityPresenterForActivity {

    @Inject
    DataProviderPresentable dataProviderPresentable;

    private List<ItemDTO> itemsDTO = new ArrayList<>();

    private boolean loadingIsLocked;
    private int currentPageNumber = 0;


    @Override
    protected void dagger2Inject() {
        MApplication.getDaggerComponents().inject(this);
    }

    @Override
    protected MainView getCastedView() {
        return (MainView) getView();
    }

    @Override
    public void loadPage(final int number, boolean fromCache) {
        if (loadingIsLocked) {
            return;
        }

        loadingIsLocked = true;

        if (fromCache) {
            for (ItemDTO itemDTO : itemsDTO) {
                getCastedView().addItem(itemDTO);
            }
            getCastedView().buildPage(itemsDTO, true);
            loadingIsLocked = false;
        } else {
            if (number == 1) {
                itemsDTO.clear();
            }

            final Observable<ItemDTO> observable = Observable.create(new Observable.OnSubscribe<ItemDTO>() {
                @Override
                public void call(Subscriber<? super ItemDTO> subscriber) {
                    dataProviderPresentable.getPageData(number, MApplication.isOnlineWithToast(false), subscriber);
                }
            });

            subscription = observable
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ItemDTO>() {
                        @Override
                        public void onCompleted() {
                            Logger.write("onCompleted()");

                            getCastedView().buildPage(itemsDTO, false);
                            dataProviderPresentable.writeItemsDTO(itemsDTO);
                            loadingIsLocked = false;
                            currentPageNumber = number;
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            loadingIsLocked = false;
                        }

                        @Override
                        public void onNext(ItemDTO itemDTO) {
//                            itemDTO.setPageNum(currentPageNumber);
                            itemsDTO.add(itemDTO);
                            getCastedView().addItem(itemDTO);
                        }
                    });
        }
    }

    @Override
    public void loadNewDataPartIfNeeded(int position) {
        if (position + 1 >= itemsDTO.size()) {
            loadPage(currentPageNumber + 1, false);
        }
    }
}
