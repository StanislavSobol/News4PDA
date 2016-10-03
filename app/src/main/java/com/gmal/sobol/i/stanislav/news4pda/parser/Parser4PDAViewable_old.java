package com.gmal.sobol.i.stanislav.news4pda.parser;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;

public interface Parser4PDAViewable_old {
    void clearData();

    NewsDTO_old getParsedNewsData();
    void parseDetailedNew(String url, CallbackBundle callbackBundle);

    DetailedNewDTO_old getParsedDetailedNewData();
}
