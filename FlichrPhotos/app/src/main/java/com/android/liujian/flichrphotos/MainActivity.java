package com.android.liujian.flichrphotos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.liujian.flichrphotos.fragments.FlickrExploreFragment;
import com.android.liujian.flichrphotos.fragments.MenuListFragment.IMenu;
import com.android.liujian.flichrphotos.view.SlidingMenu;

public class MainActivity extends FragmentActivity implements IMenu{

    private SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingmenumain);

        mSlidingMenu = (SlidingMenu)findViewById(R.id.slidingmenumain);
        mSlidingMenu.setMode(SlidingMenu.LEFT);

        View _contentView = mSlidingMenu.getContent();
        Button _toggleMenuButton = (Button)_contentView.findViewById(R.id.menu_toggle);
        
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_fragment_container);
        if(fragment == null){
        	fragment = new FlickrExploreFragment();
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
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.content_fragment_container);
        mSlidingMenu.showContent();

        if(fragment != null && fragment != currentFragment){
            fm.beginTransaction().replace(R.id.content_fragment_container, fragment).commit();
        }
    }

}
