package com.android.liujian.flichrphotos.control;

import android.widget.ImageView;

/**
 * Created by liujian on 15/9/22.
 * IDownloader interface
 */
public interface IDownloader<Model> {

    void load(final String key, final ImageView imageView, final int placeHolder);

    void queueJob(String key, ImageView imageView, int placeHolder);

    Model downloadModel(String key);

    Model getModelFromCache(String key);

    void reset();

}
