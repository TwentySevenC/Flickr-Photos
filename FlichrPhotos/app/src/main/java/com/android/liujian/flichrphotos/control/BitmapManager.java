package com.android.liujian.flichrphotos.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liujian on 15/9/16.
 * A bitmap downloader manager class
 * A singleton class
 */
public class BitmapManager {
    private static final String TAG = "BitmapManager";
    private static final int MAX_THREAD_POOL_SIZE = 5;

    private BitmapManager mBitmapManager = null;
    private final Map<String,SoftReference<Bitmap>> mCache;
    private ExecutorService mThreadPool;
    private final Map<ImageView, String> mImageViews =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private BitmapManager(){
        mCache = new HashMap<>();
        mThreadPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
    }


    public BitmapManager get(){
        if(mBitmapManager == null){
            mBitmapManager = new BitmapManager();
        }
        return mBitmapManager;
    }


    /**
     * Get the Bitmap from cache
     * @param url
     * @return a Bitmap
     */
    private Bitmap getBitmapFromCache(String url){
        if(mCache.containsKey(url)){
            return mCache.get(url).get();
        }
        return null;
    }


    /**
     * Reset the fields
     */
    public void reset(){
        ExecutorService _oldThreadPool = mThreadPool;
        mThreadPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        _oldThreadPool.shutdown();
        mCache.clear();
        mImageViews.clear();
    }

    /**
     * Load image
     * @param url url
     * @param imageView imageView
     * @param placeHolder default image
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap placeHolder){
        mImageViews.put(imageView, url);

        Bitmap _bitmap = getBitmapFromCache(url);

        if(_bitmap != null){
            imageView.setImageBitmap(_bitmap);
            Log.d(TAG, "Get bitmap from cache..");
        }else{
            imageView.setImageBitmap(placeHolder);
            queueJob(url, imageView, placeHolder);
            Log.d(TAG, "Get bitmap from network..");
        }
    }


    /**
     * Put the task in a message queue
     * @param url url
     * @param imageView imageView
     * @param placeHolder default image
     */
    private void queueJob(final String url, final ImageView imageView, final Bitmap placeHolder){

        /**Create a handler in UI thread*/
        final Handler _handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String _tag = mImageViews.get(imageView);
                if(_tag != null && _tag.equals(url)){
                    if(msg.obj != null)
                        imageView.setImageBitmap((Bitmap)msg.obj);
                    else
                        imageView.setImageBitmap(placeHolder);
                }
            }
        };

        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                Message _message = Message.obtain();
                Bitmap _bitmap = downloadBitmap(url);
                _message.obj = _bitmap;
                _handler.sendMessage(_message);
                Log.d(TAG, "Handler send a new message..");
            }
        });
    }


    /**
     * Download a Bitmap from a url
     * @param url url
     * @return a Bitmap
     */
    private Bitmap downloadBitmap(String url){

        try {
            Bitmap _bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            mCache.put(url, new SoftReference<>(_bitmap));
            return _bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
