package com.gmal.sobol.i.stanislav.news4pda.di;

import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.data.DataProvider;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA_old;
import com.gmal.sobol.i.stanislav.news4pda.presenter.details.DetailedActivityPresenter;
import com.gmal.sobol.i.stanislav.news4pda.presenter.main.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MApplication.class})
public interface RealComponents {

    void inject(Parser4PDA_old patient);

    void inject(MainActivityPresenter patient);

    void inject(DataProvider patient);

    void inject(DetailedActivityPresenter patient);
}
