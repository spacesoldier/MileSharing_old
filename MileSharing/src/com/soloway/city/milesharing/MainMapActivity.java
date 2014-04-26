package com.soloway.city.milesharing;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.gson.Gson;
import com.soloway.city.milesharing.api.UserSession;
import com.soloway.city.milesharing.routing.GMapV2GetRouteDirection;

public class MainMapActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener, OnClickListener, /*OnMapClickListener,*/ OnMapLongClickListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	
	// Different stuff for Map
	
	GoogleMap googleMap;
    
    //SharedPreferences prefs;
 
    private Button btnOk;
    private Button btnCancel;
    private CustomList adapter;
    Integer[] imageId_p = {
  	      R.drawable.pass_icon,
  	     
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none
  	  
  	  };
  
    Integer[] imageId_d = {
    	      R.drawable.driver_icon,
    	     
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none
    	  
    	  };
    List<Overlay> mapOverlays;
    GeoPoint point1, point2;
    LocationManager locManager;
    Drawable drawable;
    org.w3c.dom.Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    
    LatLng fromPosition;
    LatLng toPosition;
    MarkerOptions markerOptions;
    Location location ;
    
    SharedPreferences  prefs;
    UserSession session;
    
    private String[] drawerListViewItems;
    private ListView drawerListView;
    
    private Marker marker;
    private LinearLayout layout;
    private LinearLayout layout_top;
    
    private FragmentActivity mainMapActivity;
    int dur;
    int dis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		
		// MAP STUFF
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { // Google Play Services are available
 
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
 
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            
            googleMap.setOnMapLongClickListener(this);
            
            
            /////
            ////MY PART
            /////
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            
            drawerListViewItems = getResources().getStringArray(R.array.items_pass);
            
            // get ListView defined in activity_main.xml
            drawerListView = (ListView) findViewById(R.id.menu_drawer);
     
            adapter = new CustomList(MainMapActivity.this, drawerListViewItems, imageId_p);
            drawerListView.setAdapter(adapter);

            markerOptions = new MarkerOptions();
            
            
            fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
            
            LatLng ln = new LatLng(location.getLatitude(),location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ln));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            
            prefs = this.getSharedPreferences("com.soloway.city.milesharing", Context.MODE_PRIVATE);
          
        }
		
	}
	
	 @Override
	    public void onLocationChanged(Location location) {
	 
	 
	        // Getting latitude of the current location
	        double latitude = location.getLatitude();
	 
	        // Getting longitude of the current location
	        double longitude = location.getLongitude();
	 
	        // Creating a LatLng object for the current location
	        LatLng latLng = new LatLng(latitude, longitude);
	 
	    }
	 
	    @Override
	    public void onProviderDisabled(String provider) {
	        // TODO Auto-generated method stub
	    }
	 
	    @Override
	    public void onProviderEnabled(String provider) {
	        // TODO Auto-generated method stub
	    }
	 
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	        // TODO Auto-generated method stub
	    }
	    
	    private boolean noBtns = true;
	    
	    @Override
		public void onMapLongClick(LatLng point) {
//			googleMap.addMarker(new MarkerOptions().position(point).title("Go"));
			
			
			if (marker != null) {
				marker.remove();
	        }
	        marker = googleMap.addMarker(new MarkerOptions()
	                .position(point).title("Go")
	                .draggable(true).visible(true));
	        
	        toPosition = new LatLng(point.latitude, point.longitude);
	        
	        GetRouteTask getRoute = new GetRouteTask();
	        getRoute.execute();
	        
	        
	       
	        layout = (LinearLayout) findViewById(R.id.bottom_box);
	        layout_top = (LinearLayout) findViewById(R.id.top_box);

	        if (noBtns)
	        {
	        	
	        	Button btnOk = new Button(this);
	        	btnOk.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        	btnOk.setText("GO");
	        	btnOk.setId(R.id.btn_ok);
	        	layout.addView(btnOk);

	        	Button btnCancel = new Button(this);
	            btnCancel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	            btnCancel.setText("Cancel");
	            btnCancel.setId(R.id.btn_cancel);
	            layout.addView(btnCancel);
	        	
	            btnOk.setOnClickListener(buttonClickListener);
	            btnCancel.setOnClickListener(buttonClickListener);
	            
	            noBtns = false;
	        }
	        
		}
	    
	 
	    private OnClickListener buttonClickListener = new OnClickListener() {

	        @Override
	        public void onClick(View v){
	            switch (v.getId()) {
	                case R.id.btn_ok:
//	                	SharedPreferences  prefs = mainMapActivity.getSharedPreferences("com.soloway.city.milesharing", Context.MODE_PRIVATE);
	                	if (prefs != null)
	                	{
	                		Gson gson = new Gson();
	                	    String json = prefs.getString("UserSession", "");
	                	    session = gson.fromJson(json, UserSession.class);
	                	    
	                	    if (session != null){
	                	    	//attempt to login or register
	                	    	
	                	    } else {
	                	    	// nice. is it the first launch? Ok. let's create session!
	                	    	session = new UserSession();
	                	    	Editor prefsEditor = prefs.edit();
	                	        Gson gsonBuffer = new Gson();
	                	        String jsonBuffer = gsonBuffer.toJson(session);
	                	        prefsEditor.putString("UserSession", jsonBuffer);
	                	        prefsEditor.commit();
	                	        
	                	        // cool. so, it's time to login or register. isn't it?
	                	        
	                	    }
	                	    
//	                		String userId = prefs.getString("user_id", null);
//	                		if (userId != null){
//	                			//showRegisterDialog();// if registered - authorise (optional), check balance, receive ticket
//	                		}
//	                		else {
//	                			// registration and login
//	                		}
	                	}
	                	//showRegisterDialog();
	                    break;
	                case R.id.btn_cancel:
	                    
	                	if (marker != null) {
	            			marker.remove();
	            		}
	            		
	            		Button btn_ok = (Button) layout.findViewById(R.id.btn_ok);
	            		if (btn_ok != null) layout.removeView(btn_ok);
	            		Button button_cancel = (Button)layout.findViewById(R.id.btn_cancel);
	            		if (button_cancel != null) layout.removeView(button_cancel);
	            		noBtns = true;
	                	
	                    break;
	                case View.NO_ID:
	                default:
	                    
	                    break;
	            }
	        }
	    };   
	    
	// ROUTE LOAD SPLASH
	    private class GetRouteTask extends AsyncTask<String, Void, String> {
	        
	        private ProgressDialog Dialog;
	        String response = "";
	        @Override
	        protected void onPreExecute() {
	              Dialog = new ProgressDialog(MainMapActivity.this);
	              Dialog.setMessage("Loading route...");
	              Dialog.show();
	        }

	        @Override
	        protected String doInBackground(String... urls) {
	              //Get All Route values
	                    document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
	                    response = "Success";
	              return response;

	        }

	        @Override
	        protected void onPostExecute(String result) {
	              googleMap.clear();
	              if(response.equalsIgnoreCase("Success")){
	            	  
	             dur =  v2GetRouteDirection.getDurationValue(document);
	             dis  =  v2GetRouteDirection.getDistanceValue(document);
	              
	              
	              ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
	              PolylineOptions rectLine = new PolylineOptions().width(10).color(
	                          Color.BLUE);

	              for (int i = 0; i < directionPoint.size(); i++) {
	                    rectLine.add(directionPoint.get(i));
	              }
	             // distanceBetween(1,1,1,1);
	             // Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
	              
	              // Adding route on the map
	              googleMap.addPolyline(rectLine);
	              markerOptions.position(toPosition);
	              markerOptions.draggable(true);
	              googleMap.addMarker(markerOptions);

	              }
	             
	              Dialog.dismiss();
	        }
	  }
	    
	// OTHER STUFF    

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		/*fragmentManager
				.beginTransaction()
				.replace(R.id.content_frame,
						PlaceholderFragment.newInstance(position + 1)).commit();*/
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
//			mTitle = getString(R.string.title_section1);
			break;
		case 2:
//			mTitle = getString(R.string.title_section2);
			break;
		case 3:
//			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
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
			getMenuInflater().inflate(R.menu.activity_main_actions, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
		
//		MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_actions, menu);
// 
//        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
        case R.id.pass_item:
            //newGame();
        	
        	 
        	drawerListViewItems = getResources().getStringArray(R.array.items_pass);
        	adapter.refreshItems( drawerListViewItems, imageId_p);
        	drawerListView.setAdapter(adapter);

        	return true;
        case R.id.driver_item:


        	drawerListViewItems = getResources().getStringArray(R.array.items_driver);
        	adapter.refreshItems( drawerListViewItems, imageId_d);
        	adapter.notifyDataSetChanged();
        	//drawerListView.setAdapter(adapter);
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
		
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
