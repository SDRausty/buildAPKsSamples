package com.marakana.android.uicomponents;

import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ComponentFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		View view;

		CharSequence app_name = "NOT FOUND";
		try {
			// Load resources from UIComponent APK, not from activity APK
			Resources res = (Resources) getActivity().getPackageManager()
					.getResourcesForApplication(
							this.getClass().getPackage().getName());
			view = inflater.inflate(res.getXml(R.layout.component), null);
			app_name = res.getText(R.string.app_name);
		} catch (NameNotFoundException e) {
			// Failed to load resources from our own APK
			e.printStackTrace();
			TextView out = new TextView(getActivity());
			out.setText(app_name);
			view = out;
		}

		return view;
	}
}
