package com.gmal.sobol.i.stanislav.news4pda.parser;

import java.util.ArrayList;
import java.util.List;

public class NewsItemDTO {

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
        String detailURL;
    }

    public int size() {
        return items.size();
    }

    List<Item> getItems() {
        return items;
    }

    public Item get(int position) {
        return items.get(position);
    }

    public void addAll(NewsItemDTO src) {
        items.addAll(src.getItems());
    }

    public void add(Item item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    private List<Item> items = new ArrayList<>();
}
