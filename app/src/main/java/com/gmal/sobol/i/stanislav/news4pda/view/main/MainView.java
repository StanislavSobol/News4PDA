package com.gmal.sobol.i.stanislav.news4pda.view.main;

import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.view.BaseView;

import java.util.List;

/**
 * Created by VZ on 16.08.2016.
 */
public interface MainView extends BaseView {
    void buildPage(List<ItemDTO> itemsDTO, boolean b);

    void addItem(ItemDTO itemDTO);
}
