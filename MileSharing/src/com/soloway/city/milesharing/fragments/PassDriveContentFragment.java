package com.soloway.city.milesharing.fragments;

import com.soloway.city.milesharing.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class PassDriveContentFragment extends Fragment {
	Button cancelButton;
	PassDriveFragment parent;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewHierarchy = inflater.inflate(R.layout.fragment_pdcontent, container, false);
		cancelButton = (Button) viewHierarchy.findViewById(R.id.buttonCancel);
			
		
		cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						clos();
						//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
					}
		});
		String mytag = this.getTag();
		//�� ����� ���� ���������� ������� �������� ��� ��������� �������
		
		return viewHierarchy;
		
	}
	
	public void setParent(PassDriveFragment par){
		parent = par;
	}
	
	public void clos(){
		//parent = (PassDriveFragment) getParentFragment();
		//parent = (PassDriveFragment)  getFragmentManager().findFragmentByTag("fragmentPassDriver");
		parent = (PassDriveFragment) getParentFragment();
		parent.clos();
		//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();		
	}
	
}