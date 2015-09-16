package com.android.liujian.flichrphotos.control;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lj on 2015/8/16.
 * A Handler Thread to download the images
 */
public class ThumbnailDownloader<Token> extends HandlerThread{
    private static final String TAG = "ThumbnailDownloader";

    private static final int MESSAGE_WHAT = 0;

    private Handler mUIThreadHandler;
    private DownloadHandler mDownloadHandler;
    private Map<Token, String> mTokenStringMap;
    private ThumbnailListener<Token> mListener;


    public ThumbnailDownloader(Handler handler) {
        super(TAG);
        /**New a hasMap to store the key-value*/
        mTokenStringMap = Collections.synchronizedMap(new HashMap<Token, String>());
        mUIThreadHandler = handler;
    }

    public void queueThumbnail(Token token, String url){
        Log.d(TAG, "Get a url: " + url);

        if(!mTokenStringMap.containsValue(url)){
        	mTokenStringMap.put(token, url);
        }
        mDownloadHandler.obtainMessage(MESSAGE_WHAT, token).sendToTarget(); /**Send a message to its target handler*/
    }

    /**
     * Define a observer interface
     */
    public interface ThumbnailListener<Token>{
        void onThumbnailHandler(Token token, Bitmap bitmap);
    }


    /**
     * Set the listener
     * @param listener a instance of ThumbnailListener
     */
    public void setOnThumbnailListener(ThumbnailListener<Token> listener){
        Log.d(TAG, "setOnThumbnailListener()");
        mListener = listener;
    }

    @Override
    protected void onLooperPrepared() {
        Log.d(TAG, "onLooperPrepared()");
        mDownloadHandler = new DownloadHandler();
    }


    /**
     * A class extending Handler--DownloadHandler that
     */
    @SuppressLint("HandlerLeak")
    private class DownloadHandler extends  Handler{
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            if(MESSAGE_WHAT == msg.what){
                /**Get the message object*/
                final Token _token = (Token)msg.obj;
                /**I already get the token from UI thread, then I need get the url according to the
                 * token. Once I have the url, then I can download the bytes from network using the
                 * method FlickrUtils.getUrlBytes(). So in the method queueThumbnail(), I should
                 * save the parameters--(token, url) as a key-value, so I need define a field--map
                 */
                Log.d(TAG, "handleMessage()");
                final String _url = mTokenStringMap.get(_token);
                if(_url == null)
                    return ;

                Log.d(TAG, "Get a photo url: " + _url);

                try {
                    final Bitmap _bitmap = getBitmap(_url);
                    Log.d(TAG, "Create a bitmap.");

                    mUIThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mTokenStringMap.get(_token) != _url)
                                return ;

                            if(_bitmap != null){
//                                mTokenStringMap.remove(_token);
                                mListener.onThumbnailHandler(_token, _bitmap);
                            }

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * A method to get Bitmap thought a url
     * @param url a url refer to a photo
     * @return a photo
     * @throws IOException
     */
    private Bitmap getBitmap(String url) throws IOException{

        return FlickrUtils.getBitmapFromUrl(url);
    }

    /**
     * Clear the message and map
     */
    public void clearQueue(){
        Log.d(TAG, "clearQueue()");
        mDownloadHandler.removeMessages(MESSAGE_WHAT);
        mTokenStringMap.clear();
    }

}
