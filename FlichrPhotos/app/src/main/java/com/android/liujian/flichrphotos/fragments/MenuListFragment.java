package com.android.liujian.flichrphotos.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liujian.flichrphotos.R;

public class MenuListFragment extends Fragment {
	private static final String TAG = "SimpleListFragment";
	private IMenu mMenuStub;

	public interface IMenu{
		void replaceFragment(Fragment fragment);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_menu_content, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView menuExplore = (TextView)view.findViewById(R.id.menu_explore);
		TextView menuGallery = (TextView)view.findViewById(R.id.menu_gallery);
		TextView menuPhotographer = (TextView)view.findViewById(R.id.menu_photographer);
		TextView menuFavourite = (TextView)view.findViewById(R.id.menu_favourite);

		menuExplore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMenuStub.replaceFragment(new FlickrExploreFragment());
			}
		});

		menuGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMenuStub.replaceFragment(new FlickrGalleryFragment());
			}
		});

		menuPhotographer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMenuStub.replaceFragment(new FlickrPhotographerFragment());
			}
		});

		menuFavourite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMenuStub.replaceFragment(new FlickrFavouriteFragment());
			}
		});



	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mMenuStub = (IMenu)activity;
		super.onAttach(activity);
	}

	
}
