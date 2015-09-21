package com.android.liujian.flichrphotos;

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

		Log.d(TAG, "Before get serializable.");

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
				super.onPageSelected(position);
				Photo photo = ((BigPhotoSlideFragment)((FragmentStatePagerAdapter)mViewPager.getAdapter()).getItem(position)).getPhoto();

				if(photo != null){
					new fetchPhotoViewInfoTask().execute(photo);
				}

			}
		});

		mViewPager.setCurrentItem(position, true);


	}


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
			photo.setCommentCount(String.valueOf(Flickr.getInstance().getPhotoCommentCount(photo.getId())));
			photo.setFavCount(String.valueOf(Flickr.getInstance().getPhotoFavCount(photo.getId())));

			return photo;
		}


		@Override
		protected void onPostExecute(Photo photo) {
			mPhotoCommentCount.setText(photo.getCommentCount());
			mPhotoFavCount.setText(photo.getFavCount());
		}
	}

		
}
