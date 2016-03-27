package com.gmal.sobol.i.stanislav.news4pda.parser;

import android.os.AsyncTask;
import android.os.Build;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;
import com.gmal.sobol.i.stanislav.news4pda.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

public class Parser4PDA implements Parser4PDAViewable {
    public Parser4PDA() {
    }

    public void clearData() {
        news.clear();
    }

    public void parsePage(int number, CallbackBundle callbackBundle) {
        new ParsePageTask(callbackBundle).safeExecute("http://4pda.ru/news/page/" + number + "/");
    }

    @Override
    public NewsItemDTO getParcedData() {
        return news;
    }

    private class ParsePageTask extends AsyncTask<String, Void, Document> {

        public ParsePageTask(CallbackBundle callbackBundle) {
            this.callbackBundle = callbackBundle;
        }

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
            Document document;
            try {
                document = Jsoup.connect("http://4pda.ru/news/page/1/").get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            parseDocument(document);
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            if (document == null) {
                Logger.write("Parser4PDA IOException");
                if (callbackBundle.getError() != null) {
                    callbackBundle.getError().run();
                }
            } else {
                callbackBundle.getResult().run();
            }
        }

        private CallbackBundle callbackBundle;
    }

    synchronized private void parseDocument(Document document) {
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
