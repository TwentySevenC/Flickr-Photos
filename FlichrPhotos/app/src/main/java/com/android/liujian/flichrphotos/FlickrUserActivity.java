package com.android.liujian.flichrphotos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liujian.flichrphotos.control.PeopleDownloader;
import com.android.liujian.flichrphotos.fragments.UserAlbumFragment;
import com.android.liujian.flichrphotos.fragments.UserFavouriteFragment;
import com.android.liujian.flichrphotos.fragments.UserGroupFragment;
import com.android.liujian.flichrphotos.fragments.UserPhotostreamFragment;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.view.SlidingTabLayout;

/**
 * Created by liujian on 15/9/28.
 * Flickr user
 */
public class FlickrUserActivity extends FragmentActivity {
    private static final String TAG = "FlickrUserActivity";

    public static final String USER_ID_KEY = "flickr_user_id";
    public static final String USER_NAME_KEY = "flickr_user_name";

    private static final String[] mPagerTitles = {"Photostream", "Albums", "Favourites", "Groups"};

    private String mUserId;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle _bundle = getIntent().getExtras();
        mUserId = _bundle.getString(USER_ID_KEY);
        mUserName = _bundle.getString(USER_NAME_KEY);


        final ImageView userImage = (ImageView)findViewById(R.id.user_image);
        final TextView _userName = (TextView)findViewById(R.id.user_name);
        TextView userBrief = (TextView)findViewById(R.id.user_follow);

        PeopleDownloader _peopleDownloader = PeopleDownloader.getDownloader();
        if(_peopleDownloader == null){
            throw new NullPointerException("The PeopleDownloader instance must be created first...");
        }

        People _people = PeopleDownloader.getDownloader().getModelFromCache(mUserId);
        if(_people != null){
            userImage.setImageBitmap(_people.getBuddyicon());
            mUserName = _people.getRealName();
            _userName.setText(mUserName);
        }

        _peopleDownloader.setOnPeopleDownloadListener(new PeopleDownloader.OnPeopleDownloadListener() {
            @Override
            public void setAuthorProfile(String uerId, ImageView imageView, Bitmap bitmap, String userName) {
                if (uerId.equals(mUserId)) {
                    if (bitmap != null) {
                        userImage.setImageBitmap(bitmap);
                    }
                    if(userName != null){
                        _userName.setText(userName);
                        mUserName = userName;
                    }
                }
            }
        });


        FragmentManager _fm = getSupportFragmentManager();

        ViewPager _viewPager = (ViewPager) findViewById(R.id.user_viewpager);
        _viewPager.setAdapter(new UserPagerAdapter(_fm));

        SlidingTabLayout _slidingTabLayout = (SlidingTabLayout) findViewById(R.id.user_sliding_tabs);
        _slidingTabLayout.setViewPager(_viewPager);
    }


    /**
     * Pager adapter
     */
    private class UserPagerAdapter extends FragmentPagerAdapter{

        public UserPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return createFragment(position);
        }

        @Override
        public int getCount() {
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitles[position];
        }


        //factory-method pattern
        private Fragment createFragment(int position) {
            Fragment _fragment;

            switch (position) {
                case 0:
                    _fragment = UserPhotostreamFragment.newInstance(mUserId);
                    break;
                case 1:
                    _fragment = UserAlbumFragment.newInstance(mUserId, mUserName);
                    break;
                case 2:
                    _fragment = UserFavouriteFragment.newInstance(mUserId);
                    break;
                case 3:
                    _fragment = UserGroupFragment.newInstance(mUserId);
                    break;
                default:
                    throw new NullPointerException("Sliding tab");

            }

            return _fragment;
        }

    }

}
