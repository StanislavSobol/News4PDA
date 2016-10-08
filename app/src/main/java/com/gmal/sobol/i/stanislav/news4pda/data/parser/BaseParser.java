package com.gmal.sobol.i.stanislav.news4pda.data.parser;

import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsItemDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.DetailsMainDTO;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by VZ on 02.10.2016.
 */

public class BaseParser implements New4PDAParser {

    private final String URL = "http://4pda.ru/news/page/";

    @Override
    public List<ItemDTO> getPageData(int pageNum, Subscriber<? super ItemDTO> subscriber) {
        final List<ItemDTO> result = new ArrayList<>();
        try {
            final Document document = Jsoup.connect(URL).get();
            final Elements articles = document.getElementsByTag("article");
            for (Element article : articles) {
                if (!article.className().equals("post")) {
                    continue;
                }
                final Element link = article.getElementsByTag("a").get(0);

                final ItemDTO item = new ItemDTO();
                item.setTitle(link.attr("title"));
                item.setDetailURL(article.getElementsByTag("a").get(0).attr("href"));
                item.setDescription(article.getElementsByTag("p").get(0).text());
                item.setImageURL(article.getElementsByTag("img").get(0).attr("src"));

                result.add(item);

                if (subscriber != null) {
                    subscriber.onNext(item);
                }
            }
        } catch (IOException e) {
            if (subscriber != null) {
                subscriber.onError(e);
            }
            return null;
        }

        if (subscriber != null) {
            subscriber.onCompleted();
        }

        return result;
    }

    public DetailsMainDTO getDetailedData(String url, Subscriber<? super DetailsMainDTO> subscriber) {
        final DetailsMainDTO result = new DetailsMainDTO();
        result.setUrl(url);

        final Document document;
        try {
            document = Jsoup.connect(url).get();

            result.setTitle(document.title().replace(" - 4PDA", ""));

            final Elements metas = document.getElementsByTag("meta");
            for (Element meta : metas) {
                if (meta.attr("property").equals("og:description")) {
                    result.setDescription(meta.attr("content"));
                } else if (meta.attr("property").equals("og:image")) {
                    result.setTitleImageURL(meta.attr("content"));
                }
            }

            final Elements ps = document.getElementsByTag("p");
            for (Element p : ps) {
                final DetailsItemDTO item = new DetailsItemDTO();

                if (p.attr("style").equals("text-align: justify;")) {
                    item.setImage(false);
                    item.setContent(p.text());
                } else if (p.attr("style").equals("text-align: center;")) {
                    item.setImage(true);
                    try {
                        item.setContent(p.getElementsByTag("a").get(0).getElementsByTag("img").attr("src"));
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        // TODO To the error report
                        item.setContent("");
                    }
                }

                if (!item.getContent().isEmpty()) {
                    result.getItems().add(item);
                }
            }

        } catch (IOException e) {
            if (subscriber != null) {
                subscriber.onError(e);
            }
            return null;
        }

        if (subscriber != null) {
            subscriber.onNext(result);
            subscriber.onCompleted();
        }

        return result;
    }
}
