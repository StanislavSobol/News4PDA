package com.gmal.sobol.i.stanislav.news4pda.presenter;

import com.gmal.sobol.i.stanislav.news4pda.view.BaseActivity;
import com.gmal.sobol.i.stanislav.news4pda.view.BaseView;

import lombok.Getter;
import rx.Subscription;


abstract public class BasePresenter {

    protected Subscription subscription;
    @Getter
    private BaseView view;

    public BasePresenter() {
        dagger2Inject();
    }

    protected abstract void dagger2Inject();

    public void unsubscribe() {
        subscription.unsubscribe();
    }

    public void setView(BaseActivity baseActivity) {
        view = (BaseView) baseActivity;
    }

    abstract protected BaseView getCastedView();
}
