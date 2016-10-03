package com.gmal.sobol.i.stanislav.news4pda.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gmal.sobol.i.stanislav.news4pda.presenter.BasePresenter;
import com.gmal.sobol.i.stanislav.news4pda.presenter.PresenterUser;

import java.util.HashMap;
import java.util.Map;

abstract public class BaseActivity extends AppCompatActivity {

    private static Map<String, BasePresenter> presentersMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isRealStart()) {
            if (this instanceof PresenterUser) {
                final BasePresenter presenter = (BasePresenter) ((PresenterUser) this).createPresenter();
                presentersMap.put(this.getLocalClassName(), presenter);
            }
        }

        if (getPresenter() != null) {
            getPresenter().setView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().setView(null);

        if (!isChangingConfigurations()) {
            getPresenter().unsubscribe();
            presentersMap.remove(this.getLocalClassName());
        }
    }

    public BasePresenter getPresenter() {
        return presentersMap.get(this.getLocalClassName());
    }

    public boolean isRealStart() {
        return getPresenter() == null;
    }
}
