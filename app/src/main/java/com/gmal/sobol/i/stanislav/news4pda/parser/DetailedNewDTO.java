package com.gmal.sobol.i.stanislav.news4pda.parser;

import com.gmal.sobol.i.stanislav.news4pda.Logger;

import java.util.ArrayList;
import java.util.List;

public class DetailedNewDTO {

    public static class ContentItem {

        public boolean isImage() {
            return isImage;
        }

        public String getContent() {
            return content;
        }

        boolean isImage;
        String content = "";
    }

    public String getTitle() {
        return title;
    }

    public String getTitleImageURL() {
        return titleImageURL;
    }

    public List<ContentItem> getContentItems() {
        return contentItems;
    }

    public String getDescription() {
        return description;
    }

    void clear() {
       title = "";
       titleImageURL = "";
       contentItems.clear();
    }

    void add(ContentItem contentItem) {
        if (!contentItem.content.isEmpty()) {
            Logger.write("contentItem.content = " + contentItem.content);
            contentItems.add(contentItem);
        }
    }

    String title = "";
    String titleImageURL = "";
    List<ContentItem> contentItems = new ArrayList<>();
    String description = "";
}
