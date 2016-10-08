package com.gmal.sobol.i.stanislav.news4pda.data;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import java.util.List;

import rx.Subscriber;

/**
 * Created by VZ on 02.10.2016.
 */

public interface DataProviderPresentable {

    List<ItemDTO> getPageData(int pageNum, boolean isOnline, Subscriber<? super ItemDTO> subscriber);

    DetailsMainDTO getDetailedData(boolean isOnline, String url, Subscriber<? super DetailsMainDTO> subscriber);

    void writeItemsDTO(List<ItemDTO> itemsDTO);

    void writeDetailsMainDTO(DetailsMainDTO detailsMainDTO);
}
