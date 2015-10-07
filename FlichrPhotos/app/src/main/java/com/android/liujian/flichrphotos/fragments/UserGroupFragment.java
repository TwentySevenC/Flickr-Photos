package com.android.liujian.flichrphotos.fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liujian.flichrphotos.R;
import com.android.liujian.flichrphotos.control.Flickr;
import com.android.liujian.flichrphotos.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/9/29.
 *
 */
public class UserGroupFragment extends Fragment{
    private static final String TAG = "UserGroupFragment";

    public static final String USER_ID_KEY = "user_id_group";

    private ListView mListView;
    private ArrayList<Group> mGroups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String _userId = (String) getArguments().get(USER_ID_KEY);
        new FetchGroupsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, _userId);

    }


    public static Fragment newInstance(String userId){
        Fragment _fragment = new UserGroupFragment();
        Bundle _bundle = new Bundle();
        _bundle.putString(USER_ID_KEY, userId);
        _fragment.setArguments(_bundle);
        return _fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_user_groups, container, false);

        mListView = (ListView)_view.findViewById(R.id.user_groups);
        mListView.setOnItemClickListener(new UserGroupClickListener());
        setUpAdapter();

        return _view;
    }


    /**
     * Set a adapter for the list view
     */
    public void setUpAdapter(){
        if(getActivity() == null || mListView == null)  return ;

        if(mGroups != null)
            mListView.setAdapter(new UserGroupAdapter(mGroups));
        else
            mListView.setAdapter(null);
    }



    /**
     * An AsyncTask to download user's groups
     */
    private class FetchGroupsTask extends AsyncTask<String, Void, ArrayList<Group>> {
        @Override
        protected ArrayList<Group> doInBackground(String ... params){

            return Flickr.getInstance().getGroupsFromUser(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Group> groups) {
            mGroups = groups;

            Log.d(TAG, "Success to download the groups.");

            setUpAdapter();
        }

    }


    /**
     * ListView Adapter
     */
    private class UserGroupAdapter extends ArrayAdapter<Group> {
        public UserGroupAdapter(List<Group> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_group_item, parent, false);
            }

            ImageView _buddyicon = (ImageView)convertView.findViewById(R.id.user_group_cover_image);
            TextView _groupTitle = (TextView)convertView.findViewById(R.id.user_group_title);
            TextView _groupPhotos = (TextView)convertView.findViewById(R.id.user_group_photos);
            TextView _groupMembers = (TextView)convertView.findViewById(R.id.user_group_members);

            Group _group = mGroups.get(position);
            _groupTitle.setText(_group.getName());
            _groupPhotos.setText(_group.getPoolCount() + " photos");
            _groupMembers.setText(_group.getMemberCount() + " members");
            Bitmap _bitmap = mGroups.get(position).getBuddyicon();
            if(_bitmap != null){
                _buddyicon.setImageBitmap(_bitmap);
            }
            return convertView;
        }

    }

    /**
     * OnItemClickListener for group list
     * @author lj
     *
     */
    private class UserGroupClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        }

    }

}
