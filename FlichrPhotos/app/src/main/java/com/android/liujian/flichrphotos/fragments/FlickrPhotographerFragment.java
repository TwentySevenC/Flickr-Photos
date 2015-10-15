package com.android.liujian.flichrphotos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liujian.flichrphotos.FlickrUserActivity;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.PeopleDownloader;


/**
 * Created by liujian on 15/9/15.
 * The fragment -- 20under20
 * The inaugural celebration of the 20 most talented young photographers on Flickr.
 */
public class FlickrPhotographerFragment extends Fragment {
    private static final String TAG = "FlickrPhotographerFragment";

    private ListView mPhotographerList;
    private String[] mPhotographerNames;
    private String[] mPhotographerCoverUrls;
    private String[] mPhotographerIds;
    private String[] mPhotographerEvaluate;


    public FlickrPhotographerFragment(){
        //default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotographerNames = getResources().getStringArray(R.array.photographers_names);
        mPhotographerCoverUrls = getResources().getStringArray(R.array.photographers_cover_image_urls);
        mPhotographerIds = getResources().getStringArray(R.array.photographers_ids);
        mPhotographerEvaluate = getResources().getStringArray(R.array.photographer_author_evaluate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_photographer, container, false);

        mPhotographerList = (ListView)_view.findViewById(R.id.photographer_list);
        mPhotographerList.setOnItemClickListener(new OnListViewItemClickListener());
        setUpAdapter();

        return _view;
    }



    /**
     * Set a adapter for the list view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mPhotographerList == null)  return ;

        if(mPhotographerCoverUrls != null)
            mPhotographerList.setAdapter(new ListViewAdapter(mPhotographerCoverUrls));
        else
            mPhotographerList.setAdapter(null);
    }


    /**
     * ListView Adapter
     */
    private class ListViewAdapter extends ArrayAdapter<String>{
        public ListViewAdapter(String[] objects) {
            super(getActivity(), 0, objects);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_photographer_item, parent, false);
            }

            ImageView _photographerCover = (ImageView)convertView.findViewById(R.id.photographer_cover_photo);
            ImageView _photographerAuthorImage = (ImageView)convertView.findViewById(R.id.photographer_author_image);
            TextView _photographerAuthorName = (TextView)convertView.findViewById(R.id.photographer_author_name);
            Button _followBtn = (Button)convertView.findViewById(R.id.photographer_follow);
            TextView _PhotographerAuthorEvaluate = (TextView)convertView.findViewById(R.id.photographer_author_evaluate);

            _photographerAuthorName.setText(mPhotographerNames[position]);
            _PhotographerAuthorEvaluate.setText(mPhotographerEvaluate[position]);
            _followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "followed", Toast.LENGTH_SHORT).show();
                }
            });

            BitmapDownloader.getInstance().load(mPhotographerCoverUrls[position],
                    R.mipmap.default_image, _photographerCover);


            _photographerAuthorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(PeopleDownloader.getDownloader() == null){
                        PeopleDownloader peopleDownloader = PeopleDownloader.getInstance(new Handler());
                        peopleDownloader.start();
                        peopleDownloader.getLooper();
                    }

                    Intent _intent = new Intent(getActivity(), FlickrUserActivity.class);
                    Bundle _bundle = new Bundle();
                    _bundle.putString(FlickrUserActivity.USER_ID_KEY, mPhotographerIds[position]);
                    _bundle.putString(FlickrUserActivity.USER_NAME_KEY, mPhotographerNames[position]);
                    _intent.putExtras(_bundle);
                    startActivity(_intent);
                }
            });

            return convertView;
        }
    }


    /**
     * A item click listener
     */
    private class OnListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), mPhotographerIds[position], Toast.LENGTH_SHORT).show();
        }
    }

}
