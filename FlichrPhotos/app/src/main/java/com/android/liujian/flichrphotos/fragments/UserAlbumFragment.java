package com.android.liujian.flichrphotos.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.liujian.flichrphotos.AlbumPhotoActivity;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Photoset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/9/29.
 * User album fragment
 */
public class UserAlbumFragment extends Fragment{
    private static final String TAG = "UserAlbumFragment";

    public static final String USER_ID_KEY = "user_id";
    public static final String USER_NAME_KEY = "user_name";

    private ListView mListView;
    private ArrayList<Photoset> mAlbums;
    private String mUserId, mUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mUserId = (String) getArguments().get(USER_ID_KEY);
        mUserName = (String) getArguments().get(USER_NAME_KEY);
        new FetchPhotosetsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserId);

    }


    public static Fragment newInstance(String userId, String userName){
        UserAlbumFragment _fragment = new UserAlbumFragment();
        Bundle _bundle = new Bundle();
        _bundle.putString(USER_ID_KEY, userId);
        _bundle.putString(USER_NAME_KEY, userName);
        _fragment.setArguments(_bundle);
        return _fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_user_albums, container, false);

        mListView = (ListView)_view.findViewById(R.id.user_albums);
        mListView.setOnItemClickListener(new UserAlbumClickListener());
        setUpAdapter();

        return _view;
    }


    /**
     * Set a adapter for the list view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mListView == null)  return ;

        if(mAlbums != null)
            mListView.setAdapter(new UserAlbumAdapter(mAlbums));
        else
            mListView.setAdapter(null);
    }



    /**
     * An AsyncTask to download a user's albums
     */
    private class FetchPhotosetsTask extends AsyncTask<String, Void, ArrayList<Photoset>> {
        @Override
        protected ArrayList<Photoset> doInBackground(String ... params){

            return Flickr.getInstance().getPhotosetsList(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Photoset> photos) {
            mAlbums = photos;

            Log.d(TAG, "Success to download the user's albums.");

            setUpAdapter();
        }

    }


    /**
     * ListView Adapter
     */
    private class UserAlbumAdapter extends ArrayAdapter<Photoset> {
        public UserAlbumAdapter(List<Photoset> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_album_item, parent, false);
            }

            ImageView _imageView = (ImageView)convertView.findViewById(R.id.user_album_cover_image);
            TextView _albumName = (TextView)convertView.findViewById(R.id.user_album_title);
            TextView _albumBrief = (TextView)convertView.findViewById(R.id.user_album_brief);

            Photoset _photoset = getItem(position);
            _albumName.setText(_photoset.getTitle());
            _albumBrief.setText(_photoset.getPhotoCount() + " photos" + " - " + _photoset.getViewCount() + " views");

            BitmapDownloader.getInstance().load(mAlbums.get(position).getPrimaryPhotoUrl(), R.mipmap.default_image, _imageView);
            return convertView;
        }

    }

    /**
     * OnItemClickListener for album list
     * @author lj
     *
     */
    private class UserAlbumClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent _intent = new Intent(getActivity(), AlbumPhotoActivity.class);
            Bundle _bundle = new Bundle();
            _bundle.putString(AlbumPhotoActivity.USER_ID_KEY, mUserId);
            _bundle.putString(AlbumPhotoActivity.USER_NAME_KEY, mUserName);
            _bundle.putString(AlbumPhotoActivity.ALBUM_ID_KEY, mAlbums.get(position).getId());
            _bundle.putString(AlbumPhotoActivity.ALBUM_TITLE_KEY, mAlbums.get(position).getTitle());

            _intent.putExtras(_bundle);

            startActivity(_intent);
        }

    }

}
