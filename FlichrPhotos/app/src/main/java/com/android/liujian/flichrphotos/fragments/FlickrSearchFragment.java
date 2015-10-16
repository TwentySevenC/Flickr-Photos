package com.android.liujian.flichrphotos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.android.liujian.flichrphotos.R;

/**
 * Created by liujian on 15/10/16.
 *
 */
public class FlickrSearchFragment extends Fragment{
    private static final String TAG = "FlickrSearchFragment";

    private static final int SEARCH_PHOTO = 0;
    private static final int SEARCH_PEOPLE = 1;
    private static final int SEARCH_GROUP = 2;

    private GridView mGridView;
    private int mSearchFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Button searchPhotoBtn = (Button)view.findViewById(R.id.search_photo_btn);
        Button searchPeopleBtn = (Button)view.findViewById(R.id.search_people_btn);
        Button searchGroupBtn = (Button)view.findViewById(R.id.search_group_btn);
        mGridView = (GridView)view.findViewById(R.id.search_return_content);
        return view;
    }
}
