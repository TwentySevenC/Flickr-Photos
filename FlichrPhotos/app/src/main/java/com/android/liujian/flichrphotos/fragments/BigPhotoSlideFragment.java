package com.android.liujian.flichrphotos.fragments;

import java.io.IOException;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapManager;
import com.android.liujian.flichrphotos.control.FlickrUtils;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BigPhotoSlideFragment extends Fragment {
	private static final String TAG = "BigPhotoSlideFragment";

	private static final String FRAGMENT_ARGS_PHOTO = "photo";
	
	private Photo mPhoto;
	private ImageView mBuddyicon;
	private TextView mAuthorName;
	private AsyncTask mPhotoInfoTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPhoto = (Photo)getArguments().getSerializable(FRAGMENT_ARGS_PHOTO);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.big_photo_slide_page, container, false);
		ImageView _imageView = (ImageView) rootView.findViewById(R.id.big_photo);
		mBuddyicon = (ImageView) rootView.findViewById(R.id.photo_auther_image);
		mAuthorName = (TextView) rootView.findViewById(R.id.photo_auther_name);
		TextView _photoTitle = (TextView) rootView.findViewById(R.id.photo_title);

		_photoTitle.setText(mPhoto.getTitle());

		BitmapManager.get().loadBitmap(mPhoto.getUrl(), _imageView, R.mipmap.menu_image);
		mPhotoInfoTask = new PhotoInfoTask();
		mPhotoInfoTask.execute(mPhoto);

		return rootView;
	}
	
	/**
	 * Initialize a new fragment
	 * @return a fragment
	 */
	public static Fragment newInstance(Photo photo){
		Fragment fragment = new BigPhotoSlideFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(FRAGMENT_ARGS_PHOTO, photo);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onDestroyView() {
		mPhotoInfoTask.cancel(false);
		super.onDestroyView();
	}

	/**
     * A method to get Bitmap thought a url
     * @param url a url refer to a photo
     * @return a photo
     * @throws IOException
     */
    private Bitmap getBitmap(String url) throws IOException{

        return FlickrUtils.getBitmapFromUrl(url);
    }


	/**
	 * Get the photo's reference
	 */
	public Photo getPhoto(){
		return mPhoto;
	}


	/**
	 * An AsyncTask to download photo's information -- author name, author icon
	 */
	private final class PhotoInfoTask extends AsyncTask<Photo, Void, People> {
		@Override
		protected People doInBackground(Photo... params) {
			if(isCancelled()){
				return null;
			}
			return FlickrUtils.fetchPeople(params[0].getOwnerId());
		}

		@Override
		protected void onPostExecute(People people) {
			mAuthorName.setText(people.getRealName());
			mBuddyicon.setImageBitmap(people.getBuddyicon());
		}
	}

}
