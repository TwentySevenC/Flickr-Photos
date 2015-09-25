package com.android.liujian.flichrphotos.control;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by liujian on 15/9/24.
 * A gallery downloader
 */
public class GalleryDownloader extends HandlerThread implements IDownloader<Bitmap>{
    private static final String TAG = "GalleryDownloader";
    private static final int MESSAGE_WHAT = 2;

    private Handler mDownloadHandler;
    private Handler mUIThreadHandler;

    private final Map<String, SoftReference<Bitmap>> mCache;
    private final Map<ImageView, String> mImageViews =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private OnGalleryDownloadListener mGalleryDownloadListener;


    public GalleryDownloader(Handler handler) {
        super(TAG);
        mUIThreadHandler = handler;
        mCache = new HashMap<>();
    }


    /**Define a observer listener*/
    public interface OnGalleryDownloadListener{
        void onGalleryDownload(ImageView imageView, Bitmap bitmap);
    }

    /**
     * Set a observer listener
     * @param listener
     */
    public void setOnGalleryDownloadListener(OnGalleryDownloadListener listener){
        mGalleryDownloadListener = listener;
    }


    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mDownloadHandler = new DownloadHandler();
    }



    /**
     * A user-define handler class, use to download people and set imageView
     */
    private class DownloadHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(MESSAGE_WHAT == msg.what){
                final ImageView _imageView = (ImageView)msg.obj;
                String url = mImageViews.get(_imageView);

                if(url == null) return ;

                final Bitmap _bitmap = downloadModel(url);

                mUIThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mGalleryDownloadListener.onGalleryDownload(_imageView, _bitmap);
                    }
                });
            }
        }
    }



    @Override
    public void load(String url, int resourceId, View... imageView) {
        mImageViews.put((ImageView) imageView[0], url);

        Bitmap _bitmap = getModelFromCache(url);


        if(_bitmap != null){
            ((ImageView)imageView[0]).setImageBitmap(_bitmap);
        }else{
            ((ImageView)imageView[0]).setImageResource(resourceId);
            queueJob(url, resourceId, (ImageView)imageView[0]);
        }
    }

    @Override
    public void queueJob(String key, int resourceId, ImageView imageView) {

        mDownloadHandler.obtainMessage(MESSAGE_WHAT, imageView).sendToTarget();
    }

    @Override
    public Bitmap downloadModel(String url) {
        Bitmap _bitmap = null;

        try {
            _bitmap = FlickrUtils.getBitmapFromUrl(url);
            mCache.put(url, new SoftReference<>(_bitmap));
        } catch (IOException e) {
            Log.d(TAG, "Failed to fetch the  people..");
            e.printStackTrace();
        }

        return _bitmap;
    }

    @Override
    public Bitmap getModelFromCache(String url) {
        if(isModelInCache(url)){
            return mCache.get(url).get();
        }
        return null;
    }


    public boolean isModelInCache(String url){
        return mCache.containsKey(url);
    }

    @Override
    public void reset() {
        mDownloadHandler.removeMessages(MESSAGE_WHAT);
        mCache.clear();
        mImageViews.clear();
    }
}
