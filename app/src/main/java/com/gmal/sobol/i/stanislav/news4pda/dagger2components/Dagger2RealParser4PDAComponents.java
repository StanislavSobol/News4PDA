package com.gmal.sobol.i.stanislav.news4pda.dagger2components;

import com.gmal.sobol.i.stanislav.news4pda.News4PDAApplication;
import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={News4PDAApplication.class})
public interface Dagger2RealParser4PDAComponents {
    void inject(Parser4PDA parser4PDA);
}
