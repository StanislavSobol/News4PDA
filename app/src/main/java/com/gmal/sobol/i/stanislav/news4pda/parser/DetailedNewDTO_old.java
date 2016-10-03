package com.gmal.sobol.i.stanislav.news4pda.parser;

import java.util.ArrayList;
import java.util.List;

public class DetailedNewDTO_old {

    String title = "";
    String titleImageURL = "";
    List<ContentItem_old> contentItemOlds = new ArrayList<>();
    String description = "";

    public String getTitle() {
        return title;
    }

    public String getTitleImageURL() {
        return titleImageURL;
    }

    public List<ContentItem_old> getContentItemOlds() {
        return contentItemOlds;
    }

    public String getDescription() {
        return description;
    }

    void clear() {
       title = "";
       titleImageURL = "";
        contentItemOlds.clear();
    }

    void add(ContentItem_old contentItemOld) {
        if (!contentItemOld.content.isEmpty()) {
            contentItemOlds.add(contentItemOld);
        }
    }

    public static class ContentItem_old {

        boolean isImage;
        String content = "";

        public boolean isImage() {
            return isImage;
        }

        public String getContent() {
            return content;
        }
    }
}
