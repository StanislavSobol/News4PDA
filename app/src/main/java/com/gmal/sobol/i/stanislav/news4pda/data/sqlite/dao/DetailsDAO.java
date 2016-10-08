package com.gmal.sobol.i.stanislav.news4pda.data.sqlite.dao;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by VZ on 08.10.2016.
 */

public class DetailsDAO extends BaseDaoImpl<DetailsMainDTO, Object> {

    private DetailsItemsDAO detailsItemsDAO;// = new DetailsItemsDAO(DetailsItemDTO.class);

    public DetailsDAO(ConnectionSource connectionSource, Class<DetailsMainDTO> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        detailsItemsDAO = new DetailsItemsDAO(connectionSource, DetailsItemDTO.class);
    }

    @Override
    public DetailsMainDTO queryForId(Object o) throws SQLException {
        final DetailsMainDTO result = super.queryForId(o);
        result.getItems().addAll(detailsItemsDAO.queryForMain(result.getUrl()));
        return result;
    }


    @Override
    public CreateOrUpdateStatus createOrUpdate(DetailsMainDTO data) throws SQLException {
        final CreateOrUpdateStatus result = super.createOrUpdate(data);

        for (DetailsItemDTO item : data.getItems()) {
            detailsItemsDAO.createOrUpdate(item);
        }

        return result;
    }

    static private class DetailsItemsDAO extends BaseDaoImpl<DetailsItemDTO, Object> {

        DetailsItemsDAO(ConnectionSource connectionSource, Class<DetailsItemDTO> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }

        List<DetailsItemDTO> queryForMain(String url) throws SQLException {
            final QueryBuilder<DetailsItemDTO, Object> queryBuilder = queryBuilder();
            queryBuilder.where().eq(DetailsItemDTO.DETAILS_MAIN_FIELD, url);
            return query(queryBuilder.prepare());
        }
    }
}
