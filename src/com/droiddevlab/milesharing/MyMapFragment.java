package com.droiddevlab.milesharing;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MyMapFragment extends Fragment {

    private GoogleMap map;

    public GoogleMap getMap() {
		return map;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, null, false);

//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.container))
//            .getMap();
//

        return v;
    }
}