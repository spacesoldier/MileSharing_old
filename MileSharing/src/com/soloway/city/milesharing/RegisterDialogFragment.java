package com.soloway.city.milesharing;

import com.soloway.city.milesharing.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class RegisterDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
//	    	 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	    	    // Get the layout inflater
//	    	    LayoutInflater inflater = getActivity().getLayoutInflater();
//
//	    	    // Inflate and set the layout for the dialog
//	    	    // Pass null as the parent view because its going in the dialog layout
//	    	    builder.setView(inflater.inflate(R.layout.register_dlg, null))
//	    	    // Add action buttons
//	    	    		.setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
//	    	    			public void onClick(DialogInterface dialog, int id) {
//                   // 		Send the positive button event back to the host activity
////	    	    				mListener.onDialogPositiveClick(RegisterDialogFragment.this);
//	    	    			}
//	    	    		})
//	    	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//	    	               public void onClick(DialogInterface dialog, int id) {
//	    	            	   RegisterDialogFragment.this.getDialog().cancel();
//	    	               }
//	    	           });  
//	    	    
	    	LayoutInflater inflater = getActivity().getLayoutInflater();
	    	final AlertDialog d = new AlertDialog.Builder(getActivity())
	        .setView(inflater.inflate(R.layout.register_dlg, null))
	        .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
	        .setNegativeButton(android.R.string.cancel, null)
	        .create();

	    	d.setOnShowListener(new DialogInterface.OnShowListener() {
	    							@Override
	    							public void onShow(DialogInterface dialog) {

	    								Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
	    								b.setOnClickListener(new View.OnClickListener() {

	    										@Override
	    										public void onClick(View view) {
	    											// TODO Do something
	    											mListener.onDialogPositiveClick(RegisterDialogFragment.this);

	    											//Dismiss once everything is OK.
	    											d.dismiss();
	    										}
	    								});
	    							}
	    	});

	    	    
	    	return d;
	    }
	    
	    /* The activity that creates an instance of this dialog fragment must
	     * implement this interface in order to receive event callbacks.
	     * Each method passes the DialogFragment in case the host needs to query it. */
	    public interface RegisterDialogListener {
	        public void onDialogPositiveClick(RegisterDialogFragment dialog);
	        public void onDialogNegativeClick(RegisterDialogFragment dialog);
	    }
	    
	    // Use this instance of the interface to deliver action events
	    RegisterDialogListener mListener;
	    
	    // Override the Fragment.onAttach() method to instantiate the RegisterDialogListener
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (RegisterDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement RegisterDialogListener");
	        }
	    }
	    
	    @Override 
	    public void show(FragmentManager manager, String tag) {
	        FragmentTransaction ft = manager.beginTransaction();
	        ft.add(this, tag);
	        ft.commitAllowingStateLoss();
	        
	        
	        
	    }
	    
	   
}


