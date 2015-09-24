package com.android.liujian.flichrphotos.control;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujian on 15/9/16.
 * A bitmap downloader manager class
 * A singleton class
 */
public class BitmapDownloader implements IDownloader<Bitmap>{
    private static final String TAG = "BitmapDownloader";

    /**
     * The number of threads to keep in the pool, even if they are idle,
     * unless allowCoreThreadTimeOut is set
     */
    private static final int CORE_POOL_SIZE = 3;

    /**
     * The maximum number od threads to allow in the pool
     */
    private static final int MAX_THREAD_POOL_SIZE = 5;

    /**
     * When the number of threads is greater than the core, this is the maximum time that excess
     * idle threads will wait for new tasks before terminating
     */
    private static final long KEEP_ALIVE_TIME = 2;

    /**A singleton instance*/
    private static BitmapDownloader sBitmapDownloader = null;

    private final Map<String,SoftReference<Bitmap>> mCache;
    private ThreadPoolExecutor mThreadPool;
    private final Map<ImageView, String> mImageViews =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private BitmapDownloader(){
        mCache = new HashMap<>();
        mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_THREAD_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new BlockingLifoDeque<Runnable>());
    }



    /**Get a singleton instance*/
    public static BitmapDownloader getInstance(){
        if(sBitmapDownloader == null){
            sBitmapDownloader = new BitmapDownloader();
        }
        return sBitmapDownloader;
    }


    /**
     * Get the Bitmap from cache
     * @param url url
     * @return a Bitmap
     */
    @Override
    public Bitmap getModelFromCache(String url){
        if(mCache.containsKey(url)){
            return mCache.get(url).get();
        }
        return null;
    }


    /**
     * Reset the fields
     */
    @Override
    public void reset(){
        ExecutorService _oldThreadPool = mThreadPool;
        mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_THREAD_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new BlockingLifoDeque<Runnable>());
        _oldThreadPool.shutdown();
        mCache.clear();
        mImageViews.clear();
    }

    /**
     * Load image
     * @param url url
     * @param imageView imageView
     */
    @Override
    public void load(String url, int resourceId, View ... imageView){
        mImageViews.put((ImageView)imageView[0], url);

        Bitmap _bitmap = getModelFromCache(url);

        if(_bitmap != null){
            ((ImageView)imageView[0]).setImageBitmap(_bitmap);
            Log.d(TAG, "Get bitmap from cache..");
        }else{
            Log.d(TAG, "Get bitmap from network: " + url);

            ((ImageView)imageView[0]).setImageResource(resourceId);
            queueJob(url, resourceId, (ImageView) imageView[0]);
        }
    }


    /**
     * Put the task in a message queue
     * @param url url
     * @param imageView imageView
     */
    @Override
    public void queueJob(final String url, final int resourceId, final ImageView imageView){

        /**Create a handler in UI thread*/
        final Handler _handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String _tag = mImageViews.get(imageView);
                if(_tag != null && _tag.equals(url)){
                    if(msg.obj != null)
                        imageView.setImageBitmap((Bitmap)msg.obj);
                    else
                        imageView.setImageResource(resourceId);
                }
            }
        };

        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                Message _message = Message.obtain();
                Bitmap _bitmap = downloadModel(url);
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
    @Override
    public Bitmap downloadModel(String url){

        try {
            Bitmap _bitmap = FlickrUtils.getBitmapFromUrl(url);
            mCache.put(url, new SoftReference<>(_bitmap));
            return _bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * LIFO deque, put object to queue first and get object from queue first
     * @param <T>
     */
    static final class BlockingLifoDeque<T> extends LinkedBlockingDeque<T> {

        public BlockingLifoDeque() {
            super();
        }

        public BlockingLifoDeque(int capacity) {
            super(capacity);
        }

        public BlockingLifoDeque(Collection<? extends T> c) {
            super(c);
        }

        @Override
        public boolean add(T t) {
            super.addFirst(t);
            return true;
        }

        @Override
        public void put(T t) throws InterruptedException {
            super.putFirst(t);
        }

        @Override
        public T element() {
            return super.getFirst();
        }

        @Override
        public T peek() {
            return super.peekFirst();
        }

        @Override
        public int drainTo(Collection<? super T> c) {
            return super.drainTo(c);
        }

        @Override
        public int drainTo(Collection<? super T> c, int maxElements) {
            return super.drainTo(c, maxElements);
        }


        @Override
        public boolean offer(T t) {
            return super.offerFirst(t);
        }

        @Override
        public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
            return super.offerFirst(t, timeout, unit);
        }

        @Override
        public T remove() {
            return super.removeFirst();
        }

        @Override
        public T poll() {
            return super.pollFirst();
        }

        @Override
        public T poll(long timeout, TimeUnit unit) throws InterruptedException {
            return super.pollFirst(timeout, unit);
        }

        @Override
        public T take() throws InterruptedException {
            return super.takeFirst();
        }



        @Override
        public T pop() {
            return super.removeFirst();
        }

        @Override
        public void push(T t) {
            super.addFirst(t);
        }

        @Override
        public boolean remove(Object o) {
            return super.removeFirstOccurrence(o);
        }
    }

}
