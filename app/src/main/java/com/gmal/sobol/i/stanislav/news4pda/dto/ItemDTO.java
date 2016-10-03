package com.gmal.sobol.i.stanislav.news4pda.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
public class ItemDTO {

    private String imageURL = "";
    private String title = "";
    private String description = "";
    private String detailURL = "";
    private int pageNum;

    public boolean isValid() {
        if (imageURL.isEmpty()) return false;
        if (title.isEmpty()) return false;
        if (description.isEmpty()) return false;
        if (detailURL.isEmpty()) return false;
        return true;
    }
}
