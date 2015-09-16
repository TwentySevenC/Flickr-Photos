package com.android.liujian.flichrphotos.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.liujian.flichrphotos.R;

public class MenuListFragment extends Fragment {
	private static final String TAG = "SimpleListFragment";
	private IMenu mMenuStub;

	public interface IMenu{
		public void closeMenu();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.menu_frame, container, false);
		ListView menuListView = (ListView)view.findViewById(R.id.menu_list);
		final String[] menuItems = getResources().getStringArray(R.array.menu_list_item);
		menuListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuItems));
		
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mMenuStub.closeMenu();
				Toast.makeText(getActivity(), menuItems[position], Toast.LENGTH_SHORT).show();
			}
			
		});
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mMenuStub = (IMenu)activity;
		super.onAttach(activity);
	}	
	
}
