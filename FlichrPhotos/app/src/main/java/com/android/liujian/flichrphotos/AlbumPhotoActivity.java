package com.android.liujian.flichrphotos;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Photo;

import java.util.ArrayList;

/**
 * Created by liujian on 15/9/24.
 *
 */
public class AlbumPhotoActivity extends Activity{
    private static final String TAG = "AlbumPhotoActivity";

    public static final String USER_NAME_KEY = "user_name_album";
    public static final String USER_ID_KEY = "user_id_album";
    public static final String ALBUM_TITLE_KEY = "album_title";
    public static final String ALBUM_ID_KEY = "album_id";

    private ArrayList<Photo> mAlbumPhotos;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle _bundle = getIntent().getExtras();
        String _albumId = _bundle.getString(ALBUM_ID_KEY);
        String _userName = _bundle.getString(USER_NAME_KEY);
        String _userId = _bundle.getString(USER_ID_KEY);
        String _albumTitle = _bundle.getString(ALBUM_TITLE_KEY);
        new FetchAlbumPhotosTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, _albumId, _userId);

        setContentView(R.layout.activity_gallery);

        TextView _galleryTitle = (TextView)findViewById(R.id.single_gallery_title);
        String _title = _userName == null ? _albumTitle : _userName + "'s " + _albumTitle ;
        _galleryTitle.setText(_title);

        _galleryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mGridView = (GridView)findViewById(R.id.single_gallery_photos);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlbumPhotoActivity.this, BigPhotoPagerActivity.class);
                Bundle bundle = new Bundle();


                bundle.putSerializable(BigPhotoPagerActivity.BIG_PHOTO_ITEMS, mAlbumPhotos);

                bundle.putInt(BigPhotoPagerActivity.BIG_PHOTO_POSITION, position);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        setUpAdapter();

    }


    /**
     * Set a adapter for the Grid view
     */
    public void setUpAdapter(){
        if(mGridView == null)  return ;

        if(mAlbumPhotos != null)
            mGridView.setAdapter(new GridViewAdapter(mAlbumPhotos));
        else
            mGridView.setAdapter(null);
    }


    /**
     * GridView adapter
     */
    private class GridViewAdapter extends ArrayAdapter<Photo>{

        public GridViewAdapter(ArrayList<Photo> objects) {
            super(AlbumPhotoActivity.this, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = new ImageView(AlbumPhotoActivity.this);
            }

            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);

            BitmapDownloader.getInstance().load(getItem(position).getUrl(), R.mipmap.default_image, convertView);

            return convertView;
        }
    }


    /**
     * An AsyncTask to download album photos
     */
    private class FetchAlbumPhotosTask extends AsyncTask<String, Void, ArrayList<Photo>>{
        @Override
        protected ArrayList<Photo> doInBackground(String... params) {
            return Flickr.getInstance().getPhotosetsPhotos(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            mAlbumPhotos = photos;

            setUpAdapter();
        }
    }
}
