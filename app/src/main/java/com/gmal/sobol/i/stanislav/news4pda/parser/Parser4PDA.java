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

    public void parseNewsPage(int number, CallbackBundle callbackBundle) {
        new ParsePageTask(callbackBundle, true).safeExecute("http://4pda.ru/news/page/" + number + "/");
    }

    public NewsItemDTO getParsedNewsData() {
        return news;
    }

    public void parseDetailedNew(String url, CallbackBundle callbackBundle) {
        new ParsePageTask(callbackBundle, false).safeExecute(url);
    }

    private class ParsePageTask extends AsyncTask<String, Void, Document> {

        public ParsePageTask(CallbackBundle callbackBundle, boolean isNewsPage) {
            this.callbackBundle = callbackBundle;
            this.isNewsPage = isNewsPage;
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
                document = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


            if (isNewsPage) {
                parsePageDocument(document);
            } else {
                parseDetailedNewDocument(document);
            }
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
        private boolean isNewsPage;
    }

    synchronized private void parsePageDocument(Document document) {
        Elements articles = document.getElementsByTag("article");
        for (Element article : articles) {
            if (!article.className().equals("post")) {
                continue;
            }
            Element link = article.getElementsByTag("a").get(0);

            NewsItemDTO.Item item = new NewsItemDTO.Item();
            item.title = link.attr("title");
            item.detailURL = article.getElementsByTag("a").get(0).attr("href");
            item.description = article.getElementsByTag("p").get(0).text();
            item.imageURL = article.getElementsByTag("img").get(0).attr("src");
            news.add(item);
        }
    }

    synchronized private void parseDetailedNewDocument(Document document) {
        detailedNew.clear();
        detailedNew.title = document.title().replace(" - 4PDA","");

        Elements metas =  document.getElementsByTag("meta");
        for (Element meta : metas) {
            if (meta.attr("property").equals("og:description")) {
                detailedNew.description = meta.attr("content");
            } else if (meta.attr("property").equals("og:image")) {
                detailedNew.titleImageURL = meta.attr("content");
            }
        }

        Elements ps =  document.getElementsByTag("p");
        for (Element p : ps) {
            DetailedNewDTO.ContentItem contentItem = new DetailedNewDTO.ContentItem();


            // test
            if (p.attr("style").equals("text-align: justify;")) {
                contentItem.isImage = false;
                contentItem.content = p.text();
            } else
            if (p.attr("style").equals("text-align: center;")) {
                contentItem.isImage = true;
                contentItem.content = p.getElementsByTag("a").get(0).getElementsByTag("img").attr("src");
            }

            detailedNew.add(contentItem);
        }
    }

    private NewsItemDTO news = new NewsItemDTO();
    private DetailedNewDTO detailedNew = new DetailedNewDTO();
}
