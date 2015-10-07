package com.android.liujian.flichrphotos.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.liujian.flichrphotos.BigPhotoPagerActivity;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/9/29.
 *
 */
public class UserFavouriteFragment extends Fragment{
    private static final String TAG = "UserFavouriteFragment";

    public static final String USER_ID_KEY = "user_id_favourites";

    private ArrayList<Photo> mPhotos;
    private GridView mGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String _userId = (String) getArguments().get(USER_ID_KEY);
        new FetchFavouritesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, _userId);
    }


    public static Fragment newInstance(String userId){
        Fragment _fragment = new UserFavouriteFragment();
        Bundle _bundle = new Bundle();
        _bundle.putString(USER_ID_KEY, userId);
        _fragment.setArguments(_bundle);
        return _fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_user_favourites, container, false);
        mGridView = (GridView)_view.findViewById(R.id.user_favourites);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BigPhotoPagerActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable(BigPhotoPagerActivity.BIG_PHOTO_ITEMS, mPhotos);
                bundle.putInt(BigPhotoPagerActivity.BIG_PHOTO_POSITION, position);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        setUpAdapter();

        return _view;
    }



    /**
     * Set a adapter for the list view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mGridView == null)  return ;

        if(mPhotos != null)
            mGridView.setAdapter(new UserFavouriteAdapter(mPhotos));
        else
            mGridView.setAdapter(null);
    }


    /**
     * An AsyncTask to download a user's albums
     */
    private class FetchFavouritesTask extends AsyncTask<String, Void, ArrayList<Photo>> {
        @Override
        protected ArrayList<Photo> doInBackground(String ... params){

            return Flickr.getInstance().getPeopleFavPhotos(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            mPhotos = photos;

            Log.d(TAG, "Success to download the user's favourite photos.");

            setUpAdapter();
        }

    }



    /**
     * Photostream adapter
     */
    private class UserFavouriteAdapter extends ArrayAdapter<Photo> {

        public UserFavouriteAdapter(List<Photo> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = new ImageView(getActivity());
            }

            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);

            BitmapDownloader.getInstance().load(getItem(position).getUrl(), R.mipmap.default_image, convertView);

            return convertView;
        }
    }

}
