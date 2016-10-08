package com.gmal.sobol.i.stanislav.news4pda.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
@DatabaseTable(tableName = DetailsMainDTO.TABLE)
public class DetailsMainDTO {

    public static final String TABLE = "details";
    public static final String URL_FIELD = "url";
    public static final String TITLE_FIELD = "title";
    public static final String TITLE_IMAGE_URL = "title_image_url";
    public static final String DESCRIPTION = "description";

    @DatabaseField(id = true, columnName = URL_FIELD, dataType = DataType.STRING)
    private String url = "";
    @DatabaseField(columnName = TITLE_FIELD, dataType = DataType.STRING)
    private String title = "";
    @DatabaseField(columnName = TITLE_IMAGE_URL, dataType = DataType.STRING)
    private String titleImageURL = "";
    //    @ForeignCollectionField(eager = true)
//    private Collection<DetailsItemDTO> items = new ArrayList<>();
    @DatabaseField(columnName = DESCRIPTION, dataType = DataType.STRING)
    private String description = "";

    private List<DetailsItemDTO> items = new ArrayList<>();


    public boolean isValid() {
        if (title.isEmpty()) return false;
        if (titleImageURL.isEmpty()) return false;
        if (description.isEmpty()) return false;
        if (items.size() == 0) return false;
        if (!items.get(0).isValid()) return false;
        return true;
    }
}
