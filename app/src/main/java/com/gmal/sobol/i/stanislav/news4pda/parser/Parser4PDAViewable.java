package com.gmal.sobol.i.stanislav.news4pda.parser;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;

public interface Parser4PDAViewable {
    void clearData();
    void parsePage(int number, CallbackBundle callbackBundle);
    NewsItemDTO getParsedData();
}
