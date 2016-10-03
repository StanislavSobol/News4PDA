package com.gmal.sobol.i.stanislav.news4pda.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VZ on 16.08.2016.
 */

@Setter
@Getter
public class DetailsItemDTO {
    boolean isImage;
    String content = "";

    boolean isValid() {
//        if (isImage) return true;
//        if (!content.isEmpty()) return true;
        return true;
    }
}
