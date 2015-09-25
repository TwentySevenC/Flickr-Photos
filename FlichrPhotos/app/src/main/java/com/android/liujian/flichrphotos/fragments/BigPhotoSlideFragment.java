package com.android.liujian.flichrphotos.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapDownloader;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.control.PeopleDownloader;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;

import java.io.IOException;

public class BigPhotoSlideFragment extends Fragment {
	private static final String TAG = "BigPhotoSlideFragment";

	private static final String FRAGMENT_ARGS_PHOTO = "photo";
	
	private Photo mPhoto;
	private People mPhotoOwner;
	private ImageView mBuddyicon;
	private TextView mAuthorName;
	private TextView mPhotoTitle;
	private HiddenPhotoInfoListener mHiddenPhotoInfoListener;

	private PeopleDownloader mPeopleDownloader;

	public interface HiddenPhotoInfoListener {
		void hiddenPhotoInfo(boolean flag);
	}

	private void setOnHiddenPhotoInfoListener(HiddenPhotoInfoListener listener){
		mHiddenPhotoInfoListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPhoto = (Photo)getArguments().getSerializable(FRAGMENT_ARGS_PHOTO);
		Log.d(TAG, "Click the photo, owner id: " + mPhoto.getOwnerId());

		/**Get the people downloader thread*/
		mPeopleDownloader = PeopleDownloader.getDownloader();

		if(mPeopleDownloader == null){
			throw new NullPointerException("The PeopleDownloader instance must be created first...");
		}

		mPeopleDownloader.setOnPeopleDownloadListener(new PeopleDownloader.OnPeopleDownloadListener() {
			@Override
			public void setAuthorProfile(String userId, Bitmap bitmap, String name) {
				if (isVisible()) {

					if (!userId.equals(getPhoto().getOwnerId()))
						return;

					if (name != null) {
						mAuthorName.setText(name);
					}

					if (bitmap != null) {
						mBuddyicon.setImageBitmap(bitmap);
					}

				}
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.big_photo_slide_page, container, false);
		ImageView _imageView = (ImageView) rootView.findViewById(R.id.big_photo);
		mBuddyicon = (ImageView) rootView.findViewById(R.id.photo_author_image);
		mAuthorName = (TextView) rootView.findViewById(R.id.photo_author_name);
		mPhotoTitle = (TextView) rootView.findViewById(R.id.photo_title);

		mPhotoTitle.setText(mPhoto.getTitle());


		mBuddyicon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: Click the user buddy icon to start the user activity

				if (mPhotoOwner != null)
					Toast.makeText(getActivity(), mPhotoOwner.getUserName(), Toast.LENGTH_SHORT).show();
			}
		});



		BitmapDownloader.getInstance().load(mPhoto.getUrl(), R.mipmap.default_image, _imageView);
		mPeopleDownloader.load(mPhoto.getOwnerId(), R.mipmap.default_image, mAuthorName, mBuddyicon);


		_imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isHidden = hiddenPhotoInfo();
				mHiddenPhotoInfoListener.hiddenPhotoInfo(isHidden);
			}
		});

		return rootView;
	}
	
	/**
	 * Initialize a new fragment
	 * @return a fragment
	 */
	public static BigPhotoSlideFragment newInstance(Photo photo){
		BigPhotoSlideFragment fragment = new BigPhotoSlideFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(FRAGMENT_ARGS_PHOTO, photo);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		/**Set hidden photo information listener*/
		setOnHiddenPhotoInfoListener((HiddenPhotoInfoListener) activity);
	}


	/**
	 * Get the photo's reference
	 */
	public Photo getPhoto(){
		return mPhoto;
	}


	/**
	 * Set the profile of photo's owner
	 * @param userId  photo owner's id
	 * @param people people
	 */
	public void setAuthorProfile(String userId, People people){
		if (isVisible()) {

			if (!userId.equals(getPhoto().getOwnerId()))
				return;

			mBuddyicon.setImageBitmap(people.getBuddyicon());
			mAuthorName.setText(people.getRealName());

		}
	}




	/**
	 * When user click the big photo, hidden the photo's information
	 */
	private boolean hiddenPhotoInfo(){
		if(mPhotoTitle.getVisibility()  == View.VISIBLE){
			mPhotoTitle.setVisibility(View.GONE);
			mAuthorName.setVisibility(View.GONE);
			mBuddyicon.setVisibility(View.GONE);
			return true;
		}else{
			mPhotoTitle.setVisibility(View.VISIBLE);
			mAuthorName.setVisibility(View.VISIBLE);
			mBuddyicon.setVisibility(View.VISIBLE);
			return false;
		}

	}


	/**
	 * An AsyncTask to download photo's information -- author name, author icon
	 */

	private class PhotoInfoTask extends AsyncTask<Photo, Void, People>{
		@Override
		protected People doInBackground(Photo... params) {
			if(isCancelled()){
				return null;
			}

			People _people = null;
			Photo photo = params[0];
			Log.d(TAG, "fetchPhotoViewInfoTask. Photo id: " + photo.getId());
			try {
				_people = Flickr.getInstance().findPeopleByUserId(photo.getOwnerId());
				Log.d(TAG, "AsyncTask get a people, id: " + _people.getUserName());
			} catch (IOException e) {
				Log.d(TAG, "Fail to get people by user id.");
				e.printStackTrace();
			}

			return _people;
		}


		@Override
		protected void onPostExecute(People people) {
			mPhotoOwner = people;
			mAuthorName.setText(mPhotoOwner.getRealName());
			mBuddyicon.setImageBitmap(mPhotoOwner.getBuddyicon());
		}
	}

}
