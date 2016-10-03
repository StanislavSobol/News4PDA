package com.gmal.sobol.i.stanislav.news4pda.presenter;

public interface PresenterUser<T> {
    T createPresenter();

    T getCastedPresenter();
}
