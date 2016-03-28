package com.gmal.sobol.i.stanislav.news4pda.parser;

import java.util.ArrayList;
import java.util.List;

public class NewsDTO {

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

        public String getDetailURL() {
            return detailURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDetailURL(String detailURL) {
            this.detailURL = detailURL;
        }

        String imageURL;
        String title;
        String description;
        String detailURL;
    }

    public int size() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public Item get(int position) {
        return items.get(position);
    }

    public void addAll(NewsDTO src) {
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
