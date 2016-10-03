package com.gmal.sobol.i.stanislav.news4pda.parser;

import java.util.ArrayList;
import java.util.List;

public class NewsDTO_old {

    private List<Item> items = new ArrayList<>();

    public int size() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public Item get(int position) {
        return items.get(position);
    }

    public void addAll(NewsDTO_old src) {
        items.addAll(src.getItems());
    }

    public void add(Item item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    public static class Item {

        String imageURL;
        String title;
        String description;
        String detailURL;

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetailURL() {
            return detailURL;
        }

        public void setDetailURL(String detailURL) {
            this.detailURL = detailURL;
        }
    }
}
