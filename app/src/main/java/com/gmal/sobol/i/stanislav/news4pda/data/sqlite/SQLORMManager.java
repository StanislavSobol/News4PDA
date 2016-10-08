package com.gmal.sobol.i.stanislav.news4pda.data.sqlite;

import android.database.sqlite.SQLiteDatabase;

import com.gmal.sobol.i.stanislav.news4pda.Logger;
import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.data.sqlite.dao.DetailsDAO;
import com.gmal.sobol.i.stanislav.news4pda.data.sqlite.dao.ItemDAO;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by VZ on 08.10.2016.
 */

public class SQLORMManager extends OrmLiteSqliteOpenHelper implements SQLiteWriter, SQLiteReader {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 7;

    private ItemDAO itemDAO;
    private DetailsDAO detailsDAO;

    public SQLORMManager() {
        super(MApplication.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ItemDTO.class);
            TableUtils.createTable(connectionSource, DetailsMainDTO.class);
            TableUtils.createTable(connectionSource, DetailsItemDTO.class);
        } catch (SQLException e) {
            Logger.writeError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ItemDTO.class, true);
            TableUtils.dropTable(connectionSource, DetailsMainDTO.class, true);
            TableUtils.dropTable(connectionSource, DetailsItemDTO.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Logger.writeError(e.getMessage());
        }
    }

    @Override
    public List<ItemDTO> getPageData(int pageNum, Subscriber<? super ItemDTO> subscriber) {
        final List<ItemDTO> result = new ArrayList<>();
        try {
            final List<ItemDTO> itemDTOs = getItemDAO().selectAllForPage(pageNum);
            for (ItemDTO itemDTO : itemDTOs) {
                if (subscriber != null) {
                    subscriber.onNext(itemDTO);
                }
                result.add(itemDTO);
            }
        } catch (SQLException e) {
            if (subscriber != null) {
                subscriber.onError(e);
            }
        }
        if (subscriber != null) {
            subscriber.onCompleted();
        }
        return result;
    }

    @Override
    public DetailsMainDTO getDetailedData(String url, Subscriber<? super DetailsMainDTO> subscriber) {
        DetailsMainDTO result = null;
        try {
            result = getDetailsDAO().queryForId(url);
        } catch (SQLException e) {
            e.printStackTrace();
            if (subscriber != null) {
                subscriber.onError(e);
            }
        }
        if (subscriber != null) {
            subscriber.onNext(result);
            subscriber.onCompleted();
        }
        return result;
    }

    @Override
    public void writeItemsDTO(final List<ItemDTO> itemsDTO) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                int i = 0;
                for (ItemDTO item : itemsDTO) {
                    try {
                        if (getItemDAO().getConnectionSource().isOpen()) {
                            getItemDAO().delete(item);
                            item.setOrderWithinPage(i++);
                            getItemDAO().create(item);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void writeDetailsMainDTO(final DetailsMainDTO detailsMainDTO) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    for (DetailsItemDTO itemDTO : detailsMainDTO.getItems()) {
                        itemDTO.setDetailsMainDTO(detailsMainDTO);
                    }

                    getDetailsDAO().createOrUpdate(detailsMainDTO);
                } catch (SQLException e) {
                    Logger.writeError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private ItemDAO getItemDAO() throws SQLException {
        if (itemDAO == null) {
            itemDAO = new ItemDAO(connectionSource, ItemDTO.class);
        }
        return itemDAO;
    }

    private DetailsDAO getDetailsDAO() throws SQLException {
        if (detailsDAO == null) {
            detailsDAO = new DetailsDAO(connectionSource, DetailsMainDTO.class);
        }
        return detailsDAO;
    }
}
