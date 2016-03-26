package com.gmal.sobol.i.stanislav.news4pda.parser;

import java.util.ArrayList;
import java.util.List;

public class NewsItemDTO {

    void add(Item item) {
        items.add(item);
    }

    void clear() {
        items.clear();
    }

    public static class Item {

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        String imageURL;
        String title;
        String description;
    }

    private List<Item> items = new ArrayList<>();
}
