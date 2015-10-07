package com.android.liujian.flichrphotos.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.liujian.flichrphotos.FlickrUserActivity;
import com.android.liujian.flichrphotos.GalleryPhotoActivity;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.control.GalleryDownloader;
import com.android.liujian.flichrphotos.model.Gallery;

import java.util.ArrayList;

/**
 * Created by liujian on 15/9/15.
 * Flickr galleries
 */
public class FlickrGalleryFragment extends Fragment {
    private static final String TAG = "FlickrGalleryFragment";
    private ListView mListView;

    private ArrayList<Gallery> mGalleries;
    private GalleryDownloader mGalleryDownloader;

    public FlickrGalleryFragment(){
        //default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new FetchGalleriesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.d(TAG, "FetchGalleriesTask started...");


        mGalleryDownloader = new GalleryDownloader(new Handler());
        mGalleryDownloader.setOnGalleryDownloadListener(new GalleryDownloader.OnGalleryDownloadListener() {
            @Override
            public void onGalleryDownload(ImageView imageView, Bitmap bitmap) {
                if(isVisible()){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        mGalleryDownloader.start();
        mGalleryDownloader.getLooper();

        Log.d(TAG, "Gallery downloader started...");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mListView = (ListView)_view.findViewById(R.id.gallery);

        mListView.setOnItemClickListener(new OnListViewItemClickListener());

        setUpAdapter();

        return _view;
    }


    /**
     * Set a adapter for the grid view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mListView == null)  return ;

        if(mGalleries != null)
            mListView.setAdapter(new ListViewAdapter(mGalleries));
        else
            mListView.setAdapter(null);
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
    private class ListViewAdapter extends ArrayAdapter<Gallery>{

        public ListViewAdapter(ArrayList<Gallery> galleries) {
            super(getActivity(), 0, galleries);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_gallery_item_1, parent, false);
            }

            ImageView _galleryCoverImage = (ImageView) convertView.findViewById(R.id.gallery_primary_photo);
            TextView _galleryTitle = (TextView) convertView.findViewById(R.id.gallery_title);
            TextView _galleryUpdatedTime = (TextView) convertView.findViewById(R.id.gallery_updated_date);
            TextView _galleryDescription = (TextView) convertView.findViewById(R.id.gallery_description);
            TextView _galleryCommentCount = (TextView) convertView.findViewById(R.id.gallery_comment_count);
            TextView _galleryAuthorName = (TextView)convertView.findViewById(R.id.gallery_author_name);
            ImageView _galleryAuthorImage = (ImageView)convertView.findViewById(R.id.gallery__author_image);

            Gallery _gallery = mGalleries.get(position);
            _galleryTitle.setText(_gallery.getTitle());
            _galleryDescription.setText(_gallery.getDescription());
            _galleryCommentCount.setText(_gallery.getCommentCount());
            _galleryUpdatedTime.setText("2d");
            _galleryAuthorName.setText(_gallery.getOwnerName());

            _galleryAuthorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _intent = new Intent(getActivity(), FlickrUserActivity.class);
                    Bundle _bundle = new Bundle();
                    _bundle.putString(FlickrUserActivity.USER_ID_KEY, mGalleries.get(position).getOwnerId());
                    _bundle.putString(FlickrUserActivity.USER_NAME_KEY, mGalleries.get(position).getOwnerName());
                    _intent.putExtras(_bundle);
                    startActivity(_intent);
                }
            });


//            String _date = new SimpleDateFormat("MM-dd-yyyy").format(Date.valueOf(_gallery.getUpdatedTime()));

            mGalleryDownloader.load(mGalleries.get(position).getPrimaryPhotoUrl(), R.mipmap.default_image, _galleryCoverImage);

            return convertView;
        }

    }


    /**
     * A item click listener
     */
    private class OnListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent _intent = new Intent(getActivity(), GalleryPhotoActivity.class);
            Bundle _bundle = new Bundle();
            Gallery _gallery = mGalleries.get(position);
            _bundle.putString(GalleryPhotoActivity.GALLERY_NAME_KEY, _gallery.getTitle());
            _bundle.putString(GalleryPhotoActivity.GALLERY_ID_KEY, _gallery.getId());
            _bundle.putString(GalleryPhotoActivity.USER_NAME_KEY, _gallery.getOwnerName());
            _intent.putExtras(_bundle);
            startActivity(_intent);
        }
    }

}
