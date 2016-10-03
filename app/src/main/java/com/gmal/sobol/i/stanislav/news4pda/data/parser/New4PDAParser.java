package com.gmal.sobol.i.stanislav.news4pda.data.parser;

import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import java.util.List;

import rx.Subscriber;

/**
 * Created by VZ on 02.10.2016.
 */
public interface New4PDAParser {

    List<ItemDTO> getPageData(int pageNum, Subscriber<? super ItemDTO> subscriber);
}
