package com.gmal.sobol.i.stanislav.news4pda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    public DownloadImageTask(Context context, ImageView imageView, boolean usePool, Runnable finalRunnable) {
        for (DownloadImageTask task : currentTasks) {
            if (task.imageView == imageView) {
                task.cancel(true);
                task.removeProgressBar();
            }
        }

        this.imageView = imageView;
        this.usePool = usePool;

        if (News4PDAApplication.isOnlineWithToast(false)) {
            this.usePool = true;
        }

        this.finalRunnable = finalRunnable;

        if (imageView != null ) {
            ViewParent viewParent = imageView.getParent();
            if (viewParent != null && viewParent instanceof FrameLayout) {
                progressBar = new ProgressBar(context);
                progressBar.setMinimumWidth(imageView.getWidth());
                progressBar.setMinimumHeight(imageView.getHeight());

                parentFrameLayout = (FrameLayout) viewParent;

                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                parentFrameLayout.addView(progressBar, lParams);
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    protected void safeExecute(String url) {
        currentTasks.add(this);

        if (usePool) {
            bitmap = News4PDAApplication.getSqLiteManagerImagePool().getBitmapFromPoolByURL(url);
            if (bitmap != null) {
                onPostExecute(bitmap);
                return;
            }
        }

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
    protected Bitmap doInBackground(String... urls) {
        if (urls.length == 0) {
            return null;
        }
        if (urls[0] == null || urls[0].isEmpty()) {
            return null;
        }

        String url = urls[0];

        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            if (usePool) {
                News4PDAApplication.getSqLiteManagerImagePool().setBitmapToPool(url, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        removeProgressBar();
        bitmap = result;

        if (imageView != null) {
            imageView.setImageBitmap(result);
            imageView.setVisibility(View.VISIBLE);
        } else {
            // MainActivity.writeLog("DownloadImageTask::onPostExecute - bmImage == null");
        }

        if (finalRunnable != null) {
            finalRunnable.run();
        }

        currentTasks.remove(this);
    }

    private void removeProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            parentFrameLayout.removeView(progressBar);
        }
    }

    private static List<DownloadImageTask> currentTasks = new ArrayList<>();
    private Bitmap bitmap;

    private ImageView imageView;
    private boolean usePool;
    private Runnable finalRunnable;
    private FrameLayout parentFrameLayout;
    private ProgressBar progressBar;
}