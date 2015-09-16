package com.android.liujian.flichrphotos.fragments;


import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.liujian.flichrphotos.BigPhotoPagerActivity;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.ThumbnailDownloader;


/**
 * Create by liujian
 * A fragment manages the flickr interesting photos
 */
public class FlickrExploreFragment extends Fragment {
    private static final String TAG = "FlickrExploreFragment";
    
    private ListView mListView;
    private List<Photo> mItems;
    private ThumbnailDownloader<ImageView> mThumbnailDownloader;

    public FlickrExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        new FetchItemsTask().execute();

        /**Start the handler thread*/
        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setOnThumbnailListener(new ThumbnailDownloader.ThumbnailListener<ImageView>() {

			@Override
			public void onThumbnailHandler(ImageView imageView, Bitmap bitmap) {
				/**If the fragment is visible, set a bitmap for imageView*/
                if (isVisible()) {
                    imageView.setImageBitmap(bitmap);
                }
				
			}
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();

        Log.d(TAG, "Background thread started.");

    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_explore_photo, container, false);
		
		/**Create a tab host that including two tabs*/
        /*TabHost tabHost = (TabHost)view.findViewById(R.id.tabHost);
        
        tabHost.setup();
        
        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setLeftStripDrawable(R.color.tabStripColor);
    	tabWidget.setRightStripDrawable(R.color.tabStripColor);
    	

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                                        .setContent(R.id.tab1)
                                        .setIndicator(null, getResources().getDrawable(android.R.drawable.ic_menu_gallery));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2")
                        .setContent(R.id.tab2)
                        .setIndicator(null, getResources().getDrawable(android.R.drawable.ic_menu_search));
        tabHost.addTab(tabSpec);*/
        
        mListView = (ListView)view.findViewById(R.id.explore_image_list1);
        mListView.setOnItemClickListener(new ListPhotoClickListener());
		setUpAdapter();    /**If changed the screen's direction, view will be destroyed, the adapter need to reset again */
		return view;
	}



    /**
     * Set a adapter for the grid view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mListView == null)  return ;

        if(mItems != null)
            mListView.setAdapter(new ListViewAdapter(mItems));
        else
            mListView.setAdapter(null);
    }

    @Override
    public void onDestroy(){
        /**Quit the handler thread*/
        mThumbnailDownloader.quit();
        mThumbnailDownloader = null;

        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        mThumbnailDownloader.clearQueue();
        super.onDestroyView();
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<Photo>> {
        @Override
        protected List<Photo> doInBackground(Void ... params){

            return new Flickr().getInterestingPhotos();
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            mItems = photos;
            setUpAdapter();
        }

    }


    /**
     * ListView Adapter
     */
    private class ListViewAdapter extends ArrayAdapter<Photo>{
        public ListViewAdapter(List<Photo> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.photo_item, parent, false);
            }

            ImageView _imageView = (ImageView)convertView.findViewById(R.id.photo_item);
            mThumbnailDownloader.queueThumbnail(_imageView, mItems.get(position).getUrl());

            return convertView;
        }

    }

    /**
     * OnItemClickListener for explore photo list
     * @author lj
     *
     */
    private class ListPhotoClickListener implements OnItemClickListener{
    	
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(), BigPhotoPagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(BigPhotoPagerActivity.BIG_PHOTO, mItems.get(position).getUrl());
			
			startActivity(intent);
		}
    	
    }



}