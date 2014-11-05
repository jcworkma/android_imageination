package com.jackandabhishek.image_ination;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	public static final int CAMERA_FRAGMENT_POSITION = 0;
	public static final int BROWSEPHOTOS_FRAGMENT_POSITION = 1;
	public static final int OTHERSTUFF_FRAGMENT_POSITION = 2;
	
	private final String TAG = "Image-ination MainActivity";
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNavigationDrawerFragment =
				(NavigationDrawerFragment) getFragmentManager().findFragmentById(
						R.id.navigation_drawer);
		mTitle = getTitle();
		
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	private void LoadFragment(int position) {
		
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
			case CAMERA_FRAGMENT_POSITION:
				fragmentManager.beginTransaction()
						.replace(R.id.container, CameraFragment.newInstance()).commit();
				break;
			case BROWSEPHOTOS_FRAGMENT_POSITION:
				fragmentManager.beginTransaction()
						.replace(R.id.container, PhotoBrowserFragment.newInstance()).commit();
				break;
			case OTHERSTUFF_FRAGMENT_POSITION:
				fragmentManager.beginTransaction()
						.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
						.commit();
				break;
		}
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		LoadFragment(position);
	}
	
	public void onSectionAttached(int number) {
		switch (number) {
			case CAMERA_FRAGMENT_POSITION:
				mTitle = getString(R.string.title_section1_takephoto);
				break;
			case BROWSEPHOTOS_FRAGMENT_POSITION:
				mTitle = getString(R.string.title_section2_browsegallery);
				break;
			case OTHERSTUFF_FRAGMENT_POSITION:
				mTitle = getString(R.string.title_section3_otherstuff);
				break;
		}
	}
	
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		public PlaceholderFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
			return rootView;
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(OTHERSTUFF_FRAGMENT_POSITION);
		}
	}
	
}
