package com.gmal.sobol.i.stanislav.news4pda.data.sqlite;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import java.util.List;

import rx.Subscriber;

/**
 * Created by VZ on 08.10.2016.
 */
public interface SQLiteReader {

    List<ItemDTO> getPageData(int pageNum, Subscriber<? super ItemDTO> subscriber);

    DetailsMainDTO getDetailedData(String url, Subscriber<? super DetailsMainDTO> subscriber);
}
