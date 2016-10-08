package com.gmal.sobol.i.stanislav.news4pda.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
@DatabaseTable(tableName = ItemDTO.TABLE)
public class ItemDTO {

    public static final String TABLE = "items";

    public static final String DETAIL_URL_FIELD = "detail_url"; // id
    public static final String TITLE_FIELD = "title";
    public static final String IMAGE_URL_FIELD = "image_url";
    public static final String DESCRIPTION_FIELD = "decription";
    public static final String PAGE_NUM_FIELD = "page_num";
    public static final String ORDER_WITHIN_PAGE_FIELD = "order_within_page";

    @DatabaseField(id = true, columnName = DETAIL_URL_FIELD, dataType = DataType.STRING)
    private String detailURL = ""; // id
    @DatabaseField(columnName = TITLE_FIELD, dataType = DataType.STRING)
    private String title = "";
    @DatabaseField(columnName = IMAGE_URL_FIELD, dataType = DataType.STRING)
    private String imageURL = "";
    @DatabaseField(columnName = DESCRIPTION_FIELD, dataType = DataType.STRING)
    private String description = "";
    @DatabaseField(columnName = PAGE_NUM_FIELD, dataType = DataType.INTEGER)
    private int pageNum;
    @DatabaseField(columnName = ORDER_WITHIN_PAGE_FIELD, dataType = DataType.INTEGER)
    private int orderWithinPage;

    public boolean isValid() {
        if (imageURL.isEmpty()) return false;
        if (title.isEmpty()) return false;
        if (description.isEmpty()) return false;
        if (detailURL.isEmpty()) return false;
        return true;
    }
}
