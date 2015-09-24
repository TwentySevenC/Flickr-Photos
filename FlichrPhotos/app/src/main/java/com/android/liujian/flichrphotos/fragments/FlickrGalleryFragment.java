package com.android.liujian.flichrphotos.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Gallery;

import java.util.ArrayList;

/**
 * Created by liujian on 15/9/15.
 * Flickr galleries
 */
public class FlickrGalleryFragment extends Fragment {
    private static final String TAG = "FlickrGalleryFragment";
    private GridView mGridView;

    private ArrayList<Gallery> mGalleries;


    public FlickrGalleryFragment(){
        //default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new FetchGalleriesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.d(TAG, "FetchGalleriesTask started...");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mGridView = (GridView)_view.findViewById(R.id.gallery);

        setUpAdapter();

        return _view;
    }


    /**
     * Set a adapter for the grid view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mGridView == null)  return ;

        if(mGalleries != null)
            mGridView.setAdapter(new GridAdapter(mGalleries));
        else
            mGridView.setAdapter(null);
    }


    /**
     * An SyncTask to download galleries
     */
    private class FetchGalleriesTask extends AsyncTask<Void, Void, ArrayList<Gallery>> {
        @Override
        protected ArrayList<Gallery> doInBackground(Void ... params){

            return Flickr.getInstance().getFlickrGalleries();
        }

        @Override
        protected void onPostExecute(ArrayList<Gallery> galleries) {
            mGalleries = galleries;

            Log.d(TAG, "Success to download the galleries photos.");

            setUpAdapter();
        }

    }


    /**
     * GridView Adapter
     */
    private class GridAdapter extends ArrayAdapter<Gallery>{

        public GridAdapter(ArrayList<Gallery> galleries) {
            super(getActivity(), 0, galleries);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_gallery_item, parent, false);
            }

            ImageView _galleryCoverImage = (ImageView) convertView.findViewById(R.id.gallery_item_photo);
            TextView _galleryTitle = (TextView) convertView.findViewById(R.id.gallery_item_title);

            _galleryTitle.setText(mGalleries.get(position).getTitle());

            BitmapDownloader.getInstance()
                    .load(mGalleries.get(position).getPrimaryPhotoUrl(), R.mipmap.default_image, _galleryCoverImage);

            return convertView;
        }

    }

}
