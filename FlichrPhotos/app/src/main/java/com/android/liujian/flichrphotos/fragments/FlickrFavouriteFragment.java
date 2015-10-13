package com.android.liujian.flichrphotos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.liujian.flichrphotos.data.FlickrDataBaseHelper;
import com.android.liujian.flichrphotos.model.Photo;

import java.util.List;

/**
 * Created by liujian on 15/10/13.
 * Favourite photos
 */
public class FlickrFavouriteFragment extends Fragment{
    private static final String TAG = "FlickrFavouriteFragment";

    private List<Photo> mFavouritePhotos;
    private FlickrDataBaseHelper mDataBaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mDataBaseHelper = new FlickrDataBaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
