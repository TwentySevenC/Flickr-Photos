package com.android.liujian.flichrphotos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.liujian.flichrphotos.fragments.FlickrGalleryFragment;
import com.android.liujian.flichrphotos.fragments.MenuListFragment.IMenu;
import com.android.liujian.flichrphotos.fragments.FlickrExploreFragment;
import com.android.liujian.flichrphotos.view.SlidingMenu;

public class MainActivity extends Activity implements IMenu{

    private SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingmenumain);

        mSlidingMenu = (SlidingMenu)findViewById(R.id.slidingmenumain);
        mSlidingMenu.setMode(SlidingMenu.LEFT);

        View _contentView = mSlidingMenu.getContent();
        Button _toggleMenuButton = (Button)_contentView.findViewById(R.id.menu_toggle);
        
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_fragment_container);
        if(fragment == null){
        	fragment = new FlickrGalleryFragment();
        	fm.beginTransaction().
        	add(R.id.content_fragment_container, fragment)
        	.commit();
        }
        
        
        _toggleMenuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSlidingMenu.showMenu();
			}
		});
    }

	@Override
	public void closeMenu() {
		// TODO Auto-generated method stub
		mSlidingMenu.showContent();
	}


}
