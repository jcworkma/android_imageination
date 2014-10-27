package com.jackandabhishek.image_ination;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BrowsePhotosFragment extends Fragment {
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static BrowsePhotosFragment newInstance() {
		BrowsePhotosFragment fragment = new BrowsePhotosFragment();
		return fragment;
	}
	
	public BrowsePhotosFragment() {}
	
	@Override
	public View
			onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_browsephotos, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.BROWSEPHOTOS_FRAGMENT_POSITION);
	}
	
}
