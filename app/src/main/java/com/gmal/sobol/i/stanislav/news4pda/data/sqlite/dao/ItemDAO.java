package com.gmal.sobol.i.stanislav.news4pda.data.sqlite.dao;

import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class ItemDAO extends BaseDaoImpl<ItemDTO, Object> {

    public ItemDAO(ConnectionSource connectionSource, Class<ItemDTO> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<ItemDTO> selectAllForPage(int pageNum) throws SQLException {
        final QueryBuilder<ItemDTO, Object> queryBuilder = queryBuilder();
        queryBuilder.where().eq(ItemDTO.PAGE_NUM_FIELD, pageNum);
        queryBuilder.orderBy(ItemDTO.PAGE_NUM_FIELD, true);
        queryBuilder.orderBy(ItemDTO.ORDER_WITHIN_PAGE_FIELD, true);
        return query(queryBuilder.prepare());
    }
}
