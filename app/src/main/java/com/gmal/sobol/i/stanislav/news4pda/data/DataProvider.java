package com.gmal.sobol.i.stanislav.news4pda.data;

import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.data.parser.New4PDAParser;
import com.gmal.sobol.i.stanislav.news4pda.data.sqlite.SQLiteReader;
import com.gmal.sobol.i.stanislav.news4pda.data.sqlite.SQLiteWriter;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by VZ on 02.10.2016.
 */

public class DataProvider implements DataProviderPresentable {

    @Inject
    New4PDAParser new4PDAParser;

    @Inject
    SQLiteReader sqLiteReader;

    @Inject
    SQLiteWriter sqLiteWriter;

    public DataProvider() {
        MApplication.getDaggerComponents().inject(this);
    }

    @Override
    public List<ItemDTO> getPageData(int pageNum, boolean isOnline, Subscriber<? super ItemDTO> subscriber) {
        if (isOnline) {
            return new4PDAParser.getPageData(pageNum, subscriber);
        } else {
            return sqLiteReader.getPageData(pageNum, subscriber);
        }
    }

    public DetailsMainDTO getDetailedData(boolean isOnline, String url, Subscriber<? super DetailsMainDTO> subscriber) {
        if (isOnline) {
            return new4PDAParser.getDetailedData(url, subscriber);
        } else {
            return sqLiteReader.getDetailedData(url, subscriber);
        }
    }

    @Override
    public void writeItemsDTO(List<ItemDTO> itemsDTO) {
        sqLiteWriter.writeItemsDTO(itemsDTO);
    }

    @Override
    public void writeDetailsMainDTO(DetailsMainDTO detailsMainDTO) {
        sqLiteWriter.writeDetailsMainDTO(detailsMainDTO);
    }
}
