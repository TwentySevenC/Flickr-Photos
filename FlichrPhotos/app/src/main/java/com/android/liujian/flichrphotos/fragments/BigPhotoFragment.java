package com.android.liujian.flichrphotos.fragments;

import java.io.IOException;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.FlickrUtils;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BigPhotoFragment extends Fragment {
	private static final String TAG = "BigPhotoFragment";
	private static final String FRAGMENT_ARGS_URL = "photo_url";
	
	private String mUrl;
	private ImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mUrl = getArguments().getString(FRAGMENT_ARGS_URL);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.big_photo_slide_page, container, false);
		mImageView = (ImageView)rootView.findViewById(R.id.big_photo) ;
//		new FetchBigPhotoTask().execute(mUrl);
		new DowloadThread(mUrl).start();
		return rootView;
	}
	
	/**
	 * Initialize a new fragment
	 * @return
	 */
	public static Fragment newInstance(String url){
		Fragment fragment = new BigPhotoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(FRAGMENT_ARGS_URL, url);
		fragment.setArguments(bundle);
		return fragment;
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
     * A AsyncTask to fetch the big photo
     *
     */
    private class FetchBigPhotoTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				bitmap = getBitmap(params[0]);
			} catch (IOException e) {
				Log.d(TAG, "Failed to fetch the big photo.");
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			
			mImageView.setImageBitmap(result);
		}
		
    }


    /**
     * A test class -- new thread to download photo
     */
    public class DowloadThread extends Thread {
        private String mUrl;

        public DowloadThread(String url) {
            mUrl = url;
        }

        @Override
        public void run() {
            
            try {

                final Bitmap _bitmap = getBitmap(mUrl);
                
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(_bitmap != null)
                            mImageView.setImageBitmap(_bitmap);
                    }
                });
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }



}
