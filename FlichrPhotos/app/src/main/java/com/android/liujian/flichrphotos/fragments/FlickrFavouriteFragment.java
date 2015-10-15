package com.android.liujian.flichrphotos.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.liujian.flichrphotos.BigPhotoPagerActivity;
import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.data.FlickrDataBaseHelper;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.view.ScaleImageView;
import com.android.liujian.flichrphotos.view.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/10/13.
 * Favourite photos
 */
public class FlickrFavouriteFragment extends Fragment{
    private static final String TAG = "FlickrFavouriteFragment";

    private List<Photo> mFavouritePhotos;
    private FlickrDataBaseHelper mDataBaseHelper;
    private StaggeredGridView mGridView;
    private TextView mFavPhotoCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mDataBaseHelper = new FlickrDataBaseHelper(getActivity());

        new FetchDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mGridView = (StaggeredGridView)view.findViewById(R.id.favourite_list);
        mFavPhotoCount = (TextView)view.findViewById(R.id.favourite_photo_count);

        mGridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BigPhotoPagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BigPhotoPagerActivity.BIG_PHOTO_ITEMS, (ArrayList)mFavouritePhotos);

                bundle.putInt(BigPhotoPagerActivity.BIG_PHOTO_POSITION, position);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        int margin = getResources().getDimensionPixelSize(R.dimen.staggered_margin);

        mGridView.setItemMargin(margin);

        mGridView.setPadding(margin, 0, margin, 0);

        setUpAdapter();

        return view;
    }


    /**
     * Set a adapter for the list view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mGridView == null)  return ;

        if(mFavouritePhotos != null){
            StaggeredAdapter adapter = new StaggeredAdapter(getActivity(), R.id.staggered_item, mFavouritePhotos);
            mGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
            mGridView.setAdapter(null);
    }


    /**
     * A task to load the favourite photos from database
     */
    private class FetchDataTask extends AsyncTask<Void, Void, List<Photo>>{
        @Override
        protected List<Photo> doInBackground(Void... params) {
            return mDataBaseHelper.queryPhotos();
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            mFavouritePhotos = photos;
            mFavPhotoCount.setText(photos.size() + " photos");
            setUpAdapter();
        }
    }


    /**
     * StaggeredGridView adapter
     */
    public static class StaggeredAdapter extends ArrayAdapter<Photo>{

        public StaggeredAdapter(Context context, int resource, List<Photo> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder _holder;

            if(convertView == null){
                LayoutInflater _layoutInflater = LayoutInflater.from(getContext());
                convertView = _layoutInflater.inflate(R.layout.simple_staggered_item, parent, false);
                _holder = new ViewHolder();
                _holder.imageView = (ScaleImageView)convertView.findViewById(R.id.staggered_item);
                convertView.setTag(_holder);
            }

            _holder = (ViewHolder)convertView.getTag();

            BitmapDownloader.getInstance().load(getItem(position).getUrl(), R.mipmap.default_image, _holder.imageView);
            return convertView;
        }
    }

    static class ViewHolder{
        ScaleImageView imageView;
    }
}
