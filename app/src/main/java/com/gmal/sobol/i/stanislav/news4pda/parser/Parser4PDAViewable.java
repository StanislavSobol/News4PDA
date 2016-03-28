package com.gmal.sobol.i.stanislav.news4pda.parser;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;

public interface Parser4PDAViewable {
    void clearData();
    void parseNewsPage(int number, CallbackBundle callbackBundle);
    NewsDTO getParsedNewsData();
    void parseDetailedNew(String url, CallbackBundle callbackBundle);
    DetailedNewDTO getParsedDetailedNewData();
}
