package com.android.liujian.flichrphotos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.control.PeopleDownloader;
import com.android.liujian.flichrphotos.model.Comment;
import com.android.liujian.flichrphotos.model.Photo;

import java.util.ArrayList;

/**
 * Created by liujian on 15/9/28.
 * Comment
 */
public class CommentActivity extends Activity {
    private static final String TAG = "CommentActivity";


    public static final String COMMENT_PHOTO_ID_KEY = "comment_photo_id";
    public static final String COMMENT_PHOTO_NAME_KEY = "comment_photo_name";

    private ListView mCommentList;
    private ArrayList<Comment> mComments;
    private String mPhotoId;
    private PeopleDownloader mPeopleDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle _bundle = getIntent().getExtras();
        mPhotoId = _bundle.getString(COMMENT_PHOTO_ID_KEY);
        String _photoTitle = _bundle.getString(COMMENT_PHOTO_NAME_KEY);

        new FetchCommentsTask().execute(mPhotoId);


        mPeopleDownloader = PeopleDownloader.getDownloader();
        if(mPeopleDownloader == null){
            mPeopleDownloader = PeopleDownloader.getInstance(new Handler());
            mPeopleDownloader.start();
            mPeopleDownloader.getLooper();
        }

        mPeopleDownloader.setOnPeopleDownloadListener(new PeopleDownloader.OnPeopleDownloadListener() {
            @Override
            public void setAuthorProfile(String uerId, ImageView imageView, Bitmap bitmap, String userName) {
                if(bitmap != null)
                    imageView.setImageBitmap(bitmap);
            }
        });

        setContentView(R.layout.activity_comment);

        mCommentList = (ListView)findViewById(R.id.comment_list);
        TextView _title = (TextView)findViewById(R.id.comment_photo_title);
        _title.setText(_photoTitle);
        _title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUpAdapter();
    }


    /**
     * Set a adapter for the Grid view
     */
    public void setUpAdapter(){
        if(mCommentList == null)  return ;

        if(mComments != null)
            mCommentList.setAdapter(new CommentListAdapter(mComments));
        else
            mCommentList.setAdapter(null);
    }


    /**
     * GridView adapter
     */
    private class CommentListAdapter extends ArrayAdapter<Comment> {

        public CommentListAdapter(ArrayList<Comment> objects) {
            super(CommentActivity.this, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.activity_comment_item, parent, false);
            }

            final ImageView _authorImage = (ImageView)convertView.findViewById(R.id.comment_author_image);
            final TextView _authorName = (TextView)convertView.findViewById(R.id.comment_author_name);
            TextView _comment = (TextView)convertView.findViewById(R.id.comment);

            _authorName.setText(mComments.get(position).getAuthorName());
            _comment.setText(mComments.get(position).getContent());
            mPeopleDownloader.load(mComments.get(position).getAuthorId(), R.mipmap.default_buddyicon, _authorName, _authorImage);

            return convertView;
        }
    }


    /**
     * An AsyncTask to download photo's comment
     */
    private class FetchCommentsTask extends AsyncTask<String, Void, ArrayList<Comment>> {
        @Override
        protected ArrayList<Comment> doInBackground(String... params) {
            return Flickr.getInstance().getPhotoComments(mPhotoId);
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> comments) {
            mComments = comments;

            setUpAdapter();
        }
    }

}
