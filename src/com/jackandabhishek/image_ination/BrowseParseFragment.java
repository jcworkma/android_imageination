package com.jackandabhishek.image_ination;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class BrowseParseFragment extends Fragment {
	
	public Bitmap CurrentImage;
	private LinearLayout listLayout;
	private List<String> objectTitles;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static BrowseParseFragment newInstance() {
		BrowseParseFragment fragment = new BrowseParseFragment();
		return fragment;
	}
	
	public BrowseParseFragment() {}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(MainActivity.OTHERSTUFF_FRAGMENT_POSITION);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater infl, ViewGroup container, Bundle savedInstanceState) {
		View rootView = infl.inflate(R.layout.fragment_browseparse, container, false);
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Button button = (Button) getActivity().findViewById(R.id.browse_gallery_button1);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LaunchParse();
			}
		});
		
		((Button) getActivity().findViewById(R.id.edit_image_button1))
				.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						LaunchEditImageActivity(v);
					}
				});
		listLayout = (LinearLayout) getActivity().findViewById(R.id.browse_parse_linearlayout);
		objectTitles = new ArrayList<String>();
		LaunchParse();
	}
	
	public void LaunchParse() {
		ParseObject obj = ParseObject.create("Image");
		// while (obj != null) {
		try {
			obj.fetchFromLocalDatastore();
		}
		catch (ParseException e) {
			Log.v("Parse Retrieval", "fail");
		}
		objectTitles.add(obj.getString(Images.Media.TITLE));
		// }
		//
		// List<ParseObject> parseObjects = new ArrayList<ParseObject>();
		// try {
		// parseObjects = query.();
		// for (int i = 0; i < parseObjects.size(); i++) {
		// objectTitles.add(parseObjects.get(i).getString(Images.Media.TITLE));
		// }
		// }
		// catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		InitList();
	}
	
	private void addTextViewToListLayout(String text) {
		TextView newView = new TextView(getActivity());
		newView.setText(text);
		newView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		listLayout.addView(newView);
		return;
	}
	
	private void InitList() {
		
		if (objectTitles.size() == 0) {
			addTextViewToListLayout("Your database is empty");
		}
		else {
			for (int i = 0; i < objectTitles.size(); i++) {
				addTextViewToListLayout(objectTitles.get(i));
			}
		}
	}
	
	public void LaunchEditImageActivity(View v) {
		try {
			// Write file
			
			String filename = "bitmap12345.png";
			FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
			CurrentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
			
			// Cleanup
			stream.close();
			CurrentImage.recycle();
			
			// Pop intent
			Intent in1 = new Intent(getActivity(), EditImageActivity.class);
			in1.putExtra(EditImageActivity.IMAGE_KEY, filename);
			startActivity(in1);
		}
		catch (Exception e) {}
	}
}
