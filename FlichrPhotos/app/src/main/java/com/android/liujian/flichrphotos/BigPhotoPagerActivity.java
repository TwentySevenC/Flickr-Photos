package com.android.liujian.flichrphotos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.liujian.flichrphotos.fragments.BigPhotoFragment;

public class BigPhotoPagerActivity extends Activity {
	private static final String TAG = "BigPhotoPagerActivity";
	public static final String BIG_PHOTO = "big_photo";
	
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_flickr_photo);
		
		ImageView bigPhotoClose = (ImageView)findViewById(R.id.close_big_photo);
		bigPhotoClose.setOnClickListener(new OnClickListener() {
			/**Close big photo*/
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String url = bundle.getString(BIG_PHOTO);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.big_photo_container);
		
		if(fragment == null){
			fragment = BigPhotoFragment.newInstance(url);
			fm.beginTransaction()
			  .add(R.id.big_photo_container, fragment)
			  .commit();
		}
	}
		
}
