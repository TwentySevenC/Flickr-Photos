package com.android.liujian.flichrphotos;

import android.app.Activity;
import android.content.Context;
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
import com.android.liujian.flichrphotos.model.Gallery;
import com.android.liujian.flichrphotos.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/9/24.
 *
 */
public class GalleryPhotoActivity extends Activity{
    private static final String TAG = "GalleryPhotoActivity";

    public static final String GALLERY_KEY = "gallery";

    private Gallery mGallery;
    private ArrayList<Photo> mGalleryPhotos;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGallery = (Gallery)getIntent().getExtras().getSerializable(GALLERY_KEY);
        new FetchGalleryPhotosTask().execute(mGallery.getId());

        setContentView(R.layout.activity_gallery);

        TextView _galleryTitle = (TextView)findViewById(R.id.single_gallery_title);
        _galleryTitle.setText(mGallery.getTitle());

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
                Intent intent = new Intent(GalleryPhotoActivity.this, BigPhotoPagerActivity.class);
                Bundle bundle = new Bundle();


                bundle.putSerializable(BigPhotoPagerActivity.BIG_PHOTO_ITEMS, mGalleryPhotos);

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

        if(mGalleryPhotos != null)
            mGridView.setAdapter(new GridViewAdapter(mGalleryPhotos));
        else
            mGridView.setAdapter(null);
    }


    /**
     * GridView adapter
     */
    private class GridViewAdapter extends ArrayAdapter<Photo>{

        public GridViewAdapter(ArrayList<Photo> objects) {
            super(GalleryPhotoActivity.this, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.activity_gallery_item, parent, false);
            }

            ImageView _imageView = (ImageView)convertView.findViewById(R.id.activity_gallery_item);

            BitmapDownloader.getInstance()
                    .load(mGalleryPhotos.get(position).getUrl(), R.mipmap.default_image, _imageView);

            return convertView;
        }
    }


    /**
     * An AsyncTask to download gallery photos
     */
    private class FetchGalleryPhotosTask extends AsyncTask<String, Void, ArrayList<Photo>>{
        @Override
        protected ArrayList<Photo> doInBackground(String... params) {
            return Flickr.getInstance().getGalleryPhotos(mGallery.getId());
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            mGalleryPhotos = photos;

            setUpAdapter();
        }
    }
}
