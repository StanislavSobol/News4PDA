package com.gmal.sobol.i.stanislav.news4pda.view.details;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.view.BaseView;

/**
 * Created by VZ on 05.10.2016.
 */
public interface DetailedView extends BaseView {
    void buildPage(DetailsMainDTO data);
}
