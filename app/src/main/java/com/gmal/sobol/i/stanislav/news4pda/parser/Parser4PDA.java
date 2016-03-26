package com.gmal.sobol.i.stanislav.news4pda.parser;

import android.os.AsyncTask;
import android.os.Build;

import com.gmal.sobol.i.stanislav.news4pda.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

public class Parser4PDA implements Parser4PDAViewable {
    public Parser4PDA() {
        Logger.write("Parser4PDA constructor");
        new ParsePageTask().safeExecute("http://4pda.ru/news/page/1/");
    }

    private class ParsePageTask extends AsyncTask<String, Void, Document> {

        protected void safeExecute(String url) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                try {
                    executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                } catch (RejectedExecutionException rejectedExecutionException) {
                    rejectedExecutionException.printStackTrace();
                    try {
                        execute(url);
                    } catch (IllegalStateException illegalStateException) {
                        illegalStateException.printStackTrace();
                    }
                }
            } else {
                execute(url);
            }
        }

        @Override
        protected Document doInBackground(String... urls) {
            Logger.write("Parser4PDA doInBackground");
            Document doc = null;
            try {
                doc = Jsoup.connect("http://4pda.ru/news/page/1/").get();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.write("Parser4PDA IOException");

            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            Logger.write("Parser4PDA onPostExecute");
            parseDocument(document);
        }
    }

    private void parseDocument(Document document) {
        news.clear();

        Elements articles = document.getElementsByTag("article");
        for (Element article : articles) {
            if (!article.className().equals("post")) {
                continue;
            }
            Element link = article.getElementsByTag("a").get(0);

            NewsItemDTO.Item item = new NewsItemDTO.Item();
            item.imageURL = link.attr("href");
            item.title = link.attr("title");
            item.description = article.getElementsByTag("p").get(0).text();
            news.add(item);
        }

        Logger.write("Parser4PDA parseDocument ends");
    }

    private NewsItemDTO news = new NewsItemDTO();


}
