package com.gmal.sobol.i.stanislav.news4pda.sqlitemanager;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO;

public interface SQLiteManagerDataProvider {
    void addNewsItemDTO(NewsDTO.Item item, String srcURL);
    void loadPage(NewsDTO newsDTO, String srcURL);
}
