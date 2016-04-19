package com.gmal.sobol.i.stanislav.news4pda.parser;

import android.os.AsyncTask;
import android.os.Build;

import com.gmal.sobol.i.stanislav.news4pda.CallbackBundle;
import com.gmal.sobol.i.stanislav.news4pda.Logger;
import com.gmal.sobol.i.stanislav.news4pda.News4PDAApplication;
import com.gmal.sobol.i.stanislav.news4pda.sqlitemanager.SQLiteManagerDataProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

import javax.inject.Inject;

public class Parser4PDA implements Parser4PDAViewable {

    public Parser4PDA() {
        News4PDAApplication.getDaggerComponents().inject(this);
        Logger.write(sqLiteManagerDataProvider.toString());
    }

    public void clearData() {
        news.clear();
    }

    public void parseNewsPage(int number, CallbackBundle callbackBundle) {
        new ParsePageTask(callbackBundle, true).safeExecute("http://4pda.ru/news/page/" + number + "/");
    }

    public NewsDTO getParsedNewsData() {
        return news;
    }

    public void parseDetailedNew(String url, CallbackBundle callbackBundle) {
        new ParsePageTask(callbackBundle, false).safeExecute(url);
    }

    @Override
    public DetailedNewDTO getParsedDetailedNewData() {
        return detailedNew;
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
            Document document = null;

            if (News4PDAApplication.isOnlineWithToast(false)) {
                try {
                    document = Jsoup.connect(urls[0]).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    isError = true;
                    return null;
                }

                if (isNewsPage) {
                    parsePageDocument(document, urls[0]);
                } else {
                    parseDetailedNewDocument(document);
                }
            } else {
                if (isNewsPage) {
                    loadPageFromDB(urls[0]);
                } else {
                    // parseDetailedNewDocument(document);
                }
            }

            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            if (isError) {
                if (callbackBundle.getError() != null) {
                    callbackBundle.getError().run();
                }
            } else {
                callbackBundle.getResult().run();
            }
        }

        private CallbackBundle callbackBundle;
        private boolean isNewsPage;
        private boolean isError = false;
    }

    synchronized private void loadPageFromDB(String url) {
        sqLiteManagerDataProvider.loadPage(news, url);
    }

    synchronized private void parsePageDocument(Document document, String srcURL) {
        Elements articles = document.getElementsByTag("article");
        for (Element article : articles) {
            if (!article.className().equals("post")) {
                continue;
            }
            Element link = article.getElementsByTag("a").get(0);

            NewsDTO.Item item = new NewsDTO.Item();
            item.title = link.attr("title");
            item.detailURL = article.getElementsByTag("a").get(0).attr("href");
            item.description = article.getElementsByTag("p").get(0).text();
            item.imageURL = article.getElementsByTag("img").get(0).attr("src");
            news.add(item);

            sqLiteManagerDataProvider.addNewsItemDTO(item, srcURL);
        }
    }

    synchronized private void parseDetailedNewDocument(Document document) {
        detailedNew.clear();
        detailedNew.title = document.title().replace(" - 4PDA", "");

        Elements metas = document.getElementsByTag("meta");
        for (Element meta : metas) {
            if (meta.attr("property").equals("og:description")) {
                detailedNew.description = meta.attr("content");
            } else if (meta.attr("property").equals("og:image")) {
                detailedNew.titleImageURL = meta.attr("content");
            }
        }

        Elements ps = document.getElementsByTag("p");
        for (Element p : ps) {
            DetailedNewDTO.ContentItem contentItem = new DetailedNewDTO.ContentItem();

            if (p.attr("style").equals("text-align: justify;")) {
                contentItem.isImage = false;
                contentItem.content = p.text();
            } else if (p.attr("style").equals("text-align: center;")) {
                contentItem.isImage = true;
                try {
                    contentItem.content = p.getElementsByTag("a").get(0).getElementsByTag("img").attr("src");
                } catch (java.lang.IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    // TODO To the error report
                    contentItem.content = "";
                }
            }

            detailedNew.add(contentItem);
        }
    }

    private NewsDTO news = new NewsDTO();
    private DetailedNewDTO detailedNew = new DetailedNewDTO();
//    private SQLiteManagerDataProvider sqLiteManagerDataProvider = News4PDAApplication.getSqLiteManagerWriteable();
    @Inject
    SQLiteManagerDataProvider sqLiteManagerDataProvider;
}
