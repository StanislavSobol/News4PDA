package com.gmal.sobol.i.stanislav.news4pda;

import com.gmal.sobol.i.stanislav.news4pda.parser.Parser4PDA;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={News4PDAApplication.class})
public interface DaggerComponents {
    void inject(Parser4PDA parser4PDA);
}
