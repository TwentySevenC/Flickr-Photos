package com.android.liujian.flichrphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.android.liujian.flichrphotos.control.BitmapManager;
import com.android.liujian.flichrphotos.control.Flickr;
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

	@Override
	public void hiddenPhotoInfo() {
		if(mPhotoInfoContainer.getVisibility() == View.VISIBLE){
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
		/*Fragment fragment = fm.findFragmentById(R.id.big_photo_container);
		
		if(fragment == null){
			fragment = BigPhotoSlideFragment.newInstance(_photo);
			fm.beginTransaction()
			  .add(R.id.big_photo_container, fragment)
			  .commit();
		}*/

		mViewPager.setAdapter(new BigPhotoPagerAdapter(fm));

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				Log.d(TAG, "OnPagerSelected...");

				super.onPageSelected(position);
				mPhotoPosition = position;

//				Photo photo = ((BigPhotoSlideFragment)((FragmentStatePagerAdapter)mViewPager.getAdapter()).getItem(position)).getPhoto();
				Photo _photo = mPhotoList.get(position);

				if(_photo != null){
					new fetchPhotoViewInfoTask().execute(_photo);
				}else{
					Log.d(TAG, "Photo is null..");
				}



			}
		});

		mViewPager.setCurrentItem(position, true);


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
			return BigPhotoSlideFragment.newInstance(_photo);
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
			photo.setFavCount(String.valueOf(Flickr.getInstance().getPhotoFavCount(photo.getId())));
			photo.setCommentCount(String.valueOf(Flickr.getInstance().getPhotoCommentCount(photo.getId())));

			return photo;
		}


		@Override
		protected void onPostExecute(Photo photo) {
			mPhotoFavCount.setText(photo.getFavCount() + " Favs");
			mPhotoCommentCount.setText(photo.getCommentCount() + " Comments");
		}
	}

		
}
