package com.gmal.sobol.i.stanislav.news4pda.data.sqlite;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import java.util.List;

/**
 * Created by VZ on 08.10.2016.
 */
public interface SQLiteWriter {

    void writeItemsDTO(List<ItemDTO> itemsDTO);

    void writeDetailsMainDTO(DetailsMainDTO detailsMainDTO);
}
