package com.android.liujian.flichrphotos.control;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.android.liujian.flichrphotos.model.People;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Created by liujian on 15/9/22.
 * A singleton class
 */
public class PeopleDownloader extends HandlerThread implements IDownloader<People>{
    private static final String TAG = "PeopleDownloader";

    private static final int MESSAGE_WHAT = 1;

    private static PeopleDownloader mPeopleDownloader = null;
    private Handler mDownloadHandler;
    private Handler mUIThreadHandler;
    private final Map<String, SoftReference<People>> mCache;
    private final Map<ImageView, String> mImageViews =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());


    private OnPeopleDownloadListener mPeopleDownloadListener;


    private PeopleDownloader(Handler handler){
        super(TAG);
        mCache = new HashMap<>();
        mUIThreadHandler = handler;
    }


    /**
     * Get a PeopleDownloader instance
     * @param handler a ui thread handler
     * @return a singleton instance
     */
    public static PeopleDownloader getInstance(Handler handler){
        if(mPeopleDownloader == null){
            mPeopleDownloader = new PeopleDownloader(handler);
        }
        return mPeopleDownloader;
    }


    /**Define a observer listener*/
    public interface OnPeopleDownloadListener{
        void setAuthorProfile(String uerId, Bitmap bitmap, String userName);
    }

    /**
     * Set a observer listener
     * @param listener
     */
    public void setOnPeopleDownloadListener(OnPeopleDownloadListener listener){
        mPeopleDownloadListener = listener;
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
                ImageView _imageView = (ImageView)msg.obj;
                String userId = mImageViews.get(_imageView);

                if(userId == null) return ;

                final People _people = downloadModel(userId);

                mUIThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mPeopleDownloadListener.setAuthorProfile(_people.getId(), _people.getBuddyicon(), _people.getRealName());
                    }
                });
            }
        }
    }



    @Override
    public void load(String userId, ImageView imageView, int resourceId) {
        mImageViews.put(imageView, userId);

        People _people = getModelFromCache(userId);

        if(_people != null){
            imageView.setImageBitmap(_people.getBuddyicon());
        }else{
            imageView.setImageResource(resourceId);
            queueJob(userId, imageView, resourceId);
        }
    }


    /**Send a message to handler*/
    @Override
    public void queueJob(String key, ImageView imageView, int resourceId) {
        //TODO: Obtain a message and send to handler

        mDownloadHandler.obtainMessage(MESSAGE_WHAT, imageView).sendToTarget();
    }


    /**
     * Fetch a people
     * @param userId user id
     * @return a people instance
     */

    @Override
    public People downloadModel(String userId) {
        People people = null;

        try {
            people = Flickr.getInstance().findPeopleByUserId(userId);
            mCache.put(userId, new SoftReference<>(people));
        } catch (IOException e) {
            Log.d(TAG, "Failed to fetch the  people..");
            e.printStackTrace();
        }

        return people;
    }


    /**
     * Get a people from cache
     * @param userId use id
     * @return a people instance
     */
    @Override
    public People getModelFromCache(String userId) {
        if(mCache.containsKey(userId)){
            return mCache.get(userId).get();
        }
        return null;
    }



    @Override
    public void reset() {
        mDownloadHandler.removeMessages(MESSAGE_WHAT);
        mCache.clear();
        mImageViews.clear();
    }
}
