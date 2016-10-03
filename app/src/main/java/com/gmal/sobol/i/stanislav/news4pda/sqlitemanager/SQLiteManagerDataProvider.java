package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO_old;

public interface SQLiteManagerDataProvider {
    void addNewsItemDTO(NewsDTO_old.Item item, String srcURL);

    void loadPage(NewsDTO_old newsDTOOld, String srcURL);
}
