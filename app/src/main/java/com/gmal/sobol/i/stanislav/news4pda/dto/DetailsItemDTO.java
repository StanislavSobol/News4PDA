package com.gmal.sobol.i.stanislav.news4pda.dto;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
@DatabaseTable(tableName = DetailsItemDTO.TABLE)
public class DetailsItemDTO {

    public static final String TABLE = "details_items";
    public static final String ID_FIELD = "_id";
    public static final String IS_IMAGE_FIELD = "is_image";
    public static final String CONTENT_FIELD = "content";
    public static final String DETAILS_MAIN_FIELD = "detail_id";

    @DatabaseField(id = true, columnName = ID_FIELD, dataType = DataType.UUID)
    private UUID id = UUID.randomUUID();
    @DatabaseField(columnName = IS_IMAGE_FIELD, dataType = DataType.BOOLEAN)
    private boolean isImage;
    @DatabaseField(columnName = CONTENT_FIELD, dataType = DataType.STRING)
    private String content = "";
    @DatabaseField(columnName = DETAILS_MAIN_FIELD, foreign = true)
    private DetailsMainDTO detailsMainDTO;

//    @ForeignCollectionField(eager = true)
//    @DatabaseField(columnName = DETAILS_MAIN_FIELD)
//    DetailsMainDTO detailsMainDTO;

    boolean isValid() {
        if (isImage) return true;
        if (!content.isEmpty()) return true;
        return true;
    }
}
