package com.android.liujian.flichrphotos.fragments;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.liujian.flichrphotos.BigPhotoPagerActivity;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.R;


/**
 * Create by liujian
 * A fragment manages the flickr interesting photos
 */
public class FlickrExploreFragment extends Fragment {
    private static final String TAG = "FlickrExploreFragment";
    
    private ListView mListView;
    private ArrayList<Photo> mItems;
    private EditText mSearchTxt;
    private Button mSearchCancelBtn;

    public FlickrExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        new FetchItemsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_explore, container, false);
		
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
        ImageView searchImage = (ImageView)view.findViewById(R.id.search_image);
        final ImageView clearSearchTxt = (ImageView)view.findViewById(R.id.clear_search_text);
        mSearchTxt = (EditText)view.findViewById(R.id.search_txt);
        mSearchCancelBtn = (Button)view.findViewById(R.id.search_cancel);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchCancelBtn.setVisibility(View.VISIBLE);
            }
        });

        mSearchTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mSearchCancelBtn.getVisibility() != View.VISIBLE)
                    mSearchCancelBtn.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mSearchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSearchTxt.getText().toString().trim().equals("")) {
                    clearSearchTxt.setVisibility(View.VISIBLE);
                } else {
                    clearSearchTxt.setVisibility(View.INVISIBLE);
                }
            }
        });

        clearSearchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTxt.setText("");
                clearSearchTxt.setVisibility(View.INVISIBLE);
            }
        });

        mSearchCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTxt.setText("");
                clearSearchTxt.setVisibility(View.INVISIBLE);
                mSearchCancelBtn.setVisibility(View.INVISIBLE);
            }
        });
        
        mListView = (ListView)view.findViewById(R.id.explore_image_list1);
        mListView.setOnItemClickListener(new ListPhotoClickListener());
		setUpAdapter();    /**If changed the screen's direction, view will be destroyed, the adapter need to reset again */
		return view;
	}



    /**
     * Set a adapter for the list view
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

        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * An AsyncTask to download photo items
     */
    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<Photo>> {
        @Override
        protected ArrayList<Photo> doInBackground(Void ... params){

            return Flickr.getInstance().getInterestingPhotos();
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            mItems = photos;

            Log.d(TAG, "Success to download the interesting photos.");

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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_photo_item, parent, false);
            }

            ImageView _imageView = (ImageView)convertView.findViewById(R.id.photo_item);

            BitmapDownloader.getInstance().load(mItems.get(position).getUrl(), R.mipmap.default_image, _imageView);
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
            bundle.putSerializable(BigPhotoPagerActivity.BIG_PHOTO_ITEMS, mItems);
            bundle.putInt(BigPhotoPagerActivity.BIG_PHOTO_POSITION, position);
            intent.putExtras(bundle);
			
			startActivity(intent);
		}
    	
    }

}
