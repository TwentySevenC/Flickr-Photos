package com.android.liujian.flichrphotos.control;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by liujian on 15/9/22.
 * IDownloader interface
 */
public interface IDownloader<Model> {

    void load(final String key, final int resourceId, final View ... imageView);

    void queueJob(String key, int resourceId, ImageView imageView);

    Model downloadModel(String key);

    Model getModelFromCache(String key);

    void reset();

}
