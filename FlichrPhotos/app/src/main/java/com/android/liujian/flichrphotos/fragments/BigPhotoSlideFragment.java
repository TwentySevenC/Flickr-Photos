package com.android.liujian.flichrphotos.fragments;

import java.io.IOException;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.BitmapManager;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.control.FlickrUtils;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BigPhotoSlideFragment extends Fragment {
	private static final String TAG = "BigPhotoSlideFragment";

	private static final String FRAGMENT_ARGS_PHOTO = "photo";
	
	private Photo mPhoto;
	private People mPhotoOwner;
	private ImageView mBuddyicon;
	private TextView mAuthorName;
	private AsyncTask mPhotoInfoTask;
	private TextView mPhotoTitle;
	private HiddenPhotoInfoListener mHiddenPhotoInfoListener;

	public interface HiddenPhotoInfoListener {
		void hiddenPhotoInfo();
	}

	private void setOnHiddenPhotoInfoListener(HiddenPhotoInfoListener listener){
		mHiddenPhotoInfoListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPhoto = (Photo)getArguments().getSerializable(FRAGMENT_ARGS_PHOTO);
		Log.d(TAG, "Click the photo owner id: " + mPhoto.getOwnerId());
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
				/**TODO:
				 * Click the user buddy icon to start the user activity
				 */
				Toast.makeText(getActivity(), mPhotoOwner.getUserName(), Toast.LENGTH_SHORT).show();
			}
		});



		BitmapManager.getInstance().loadBitmap(mPhoto.getUrl(), _imageView, R.mipmap.menu_image);
		mPhotoInfoTask = new PhotoInfoTask();
		mPhotoInfoTask.execute(mPhoto.getOwnerId());

		_imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hiddenPhotoInfo();
				mHiddenPhotoInfoListener.hiddenPhotoInfo();
			}
		});

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


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		/**Set hidden photo information listener*/
		setOnHiddenPhotoInfoListener((HiddenPhotoInfoListener)activity);
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
	 * When user click the big photo, hidden the photo's information
	 */
	private void hiddenPhotoInfo(){
		if(mPhotoTitle.getVisibility()  == View.VISIBLE){
			mPhotoTitle.setVisibility(View.GONE);
			mAuthorName.setVisibility(View.GONE);
			mBuddyicon.setVisibility(View.GONE);
		}else{
			mPhotoTitle.setVisibility(View.VISIBLE);
			mAuthorName.setVisibility(View.VISIBLE);
			mBuddyicon.setVisibility(View.VISIBLE);
		}

	}


	/**
	 * An AsyncTask to download photo's information -- author name, author icon
	 */
	private final class PhotoInfoTask extends AsyncTask<String, Void, People> {
		@Override
		protected People doInBackground(String... params) {
			if(isCancelled()){
				return null;
			}

			People _people = null;
			try {
				_people = Flickr.getInstance().findPeopleByUserId(params[0]);
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
