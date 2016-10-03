package com.gmal.sobol.i.stanislav.news4pda.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
public class DetailsMainDTO {
    String title = "";
    String titleImageURL = "";
    List<DetailsItemDTO> items = new ArrayList<>();
    String description = "";

    public boolean isValid() {
        if (title.isEmpty()) return false;
        if (titleImageURL.isEmpty()) return false;
        if (description.isEmpty()) return false;
        if (items.size() == 0) return false;
        if (!items.get(0).isValid()) return false;
        return true;
    }
}
