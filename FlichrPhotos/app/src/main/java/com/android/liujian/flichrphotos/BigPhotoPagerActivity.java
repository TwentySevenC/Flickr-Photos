package com.android.liujian.flichrphotos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.control.PeopleDownloader;
import com.android.liujian.flichrphotos.fragments.BigPhotoSlideFragment;
import com.android.liujian.flichrphotos.model.Photo;


import java.util.ArrayList;

public class BigPhotoPagerActivity extends FragmentActivity  implements BigPhotoSlideFragment.HiddenPhotoInfoListener{
	private static final String TAG = "BigPhotoPagerActivity";
	public static final String BIG_PHOTO_ITEMS = "big_photo_items";
	public static final String BIG_PHOTO_POSITION = "position";
	
	public ViewPager mViewPager;
	private ArrayList<Photo> mPhotoList;
	private TextView mPhotoCommentCount;
	private TextView mPhotoFavCount;
	private RelativeLayout mPhotoInfoContainer;
	private ImageView mClosePhotoImage;
	private int mPhotoPosition;

	private PeopleDownloader mPeopleDownloader;

	@Override
	public void hiddenPhotoInfo(boolean isHidden) {
		if(isHidden){
			mPhotoInfoContainer.setVisibility(View.GONE);
			mClosePhotoImage.setVisibility(View.GONE);
		}else{
			mPhotoInfoContainer.setVisibility(View.VISIBLE);
			mClosePhotoImage.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_flickr_photo);
		mViewPager = (ViewPager)findViewById(R.id.photo_pager);
		mPhotoCommentCount = (TextView)findViewById(R.id.photo_num_comments);
		mPhotoFavCount = (TextView)findViewById(R.id.photo_num_faves);
		mPhotoInfoContainer = (RelativeLayout)findViewById(R.id.photo_info_container);
		mClosePhotoImage = (ImageView)findViewById(R.id.close_big_photo);
		
		mClosePhotoImage = (ImageView)findViewById(R.id.close_big_photo);
		mClosePhotoImage.setOnClickListener(new OnClickListener() {
			/**Close big photo*/
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Bundle bundle = getIntent().getExtras();

		mPhotoList = (ArrayList<Photo>)bundle.getSerializable(BIG_PHOTO_ITEMS);
		int position = bundle.getInt(BIG_PHOTO_POSITION);
		
		FragmentManager fm = getSupportFragmentManager();



		/**Start the PeopleDownloader thread if the downloader was not started yet*/
		mPeopleDownloader = PeopleDownloader.getDownloader();

		if(mPeopleDownloader == null){
			mPeopleDownloader = PeopleDownloader.getInstance(new Handler());
			mPeopleDownloader.start();
			mPeopleDownloader.getLooper();
		}


		Log.d(TAG, "PeopleDownloader started...");


		mViewPager.setAdapter(new BigPhotoPagerAdapter(fm));

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Log.d(TAG, "OnPagerSelected...");

				super.onPageSelected(position);
				mPhotoPosition = position;

				Photo _photo = mPhotoList.get(position);

				if (_photo != null) {
					new fetchPhotoViewInfoTask().execute(_photo);

				}


			}
		});

		mViewPager.setCurrentItem(position, true);


	}

	@Override
	protected void onDestroy() {
		mPeopleDownloader.reset();
		super.onDestroy();
	}

	/******************************Handle some onClick events***********************************/

	public void photoShare(View v){
		//TODO: Share the photo to some other app
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, mPhotoList.get(mPhotoPosition).getUrl());
		shareIntent.setType("image/*");
		startActivity(Intent.createChooser(shareIntent, "Share image to..."));
	}


	public void addPhotoFavourite(View v){
		//TODO: add photo as a favourite one

	}

	public void showPhotoComments(View v){
		//TODO: show photo comments

	}


	public void showPhotoInfo(View v){
		//TODO: show photo information
	}


	/********************************************************************************************/


	/**
	 * Pager adapter
	 */
	private class BigPhotoPagerAdapter extends FragmentStatePagerAdapter{

		public BigPhotoPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Photo _photo = mPhotoList.get(position);

			BigPhotoSlideFragment _fragment = BigPhotoSlideFragment.newInstance(_photo);

			if(PeopleDownloader.getDownloader() != null && PeopleDownloader.getDownloader().isModelInCache(_photo.getOwnerId())){
				_fragment.setAuthorProfile(_photo.getOwnerId(), PeopleDownloader.getDownloader().getModelFromCache(_photo.getOwnerId()));
			}

			return _fragment;
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}
	}


	/**
	 * An AsyncTask to download photo's comments count and views count
	 */
	private class fetchPhotoViewInfoTask extends AsyncTask<Photo, Void, Photo>{
		@Override
		protected Photo doInBackground(Photo... params) {
			Photo photo = params[0];
			Log.d(TAG, "fetchPhotoViewInfoTask. Photo id: " + photo.getId());
			if(photo.getCommentCount() != null){

				return photo;

			}else{

				photo.setFavCount(String.valueOf(Flickr.getInstance().getPhotoFavCount(photo.getId())));
				photo.setCommentCount(String.valueOf(Flickr.getInstance().getPhotoCommentCount(photo.getId())));

				return photo;
			}

		}


		@Override
		protected void onPostExecute(Photo photo) {
			mPhotoFavCount.setText(photo.getFavCount() + " Favs");
			mPhotoCommentCount.setText(photo.getCommentCount() + " Comments");
		}
	}

		
}
