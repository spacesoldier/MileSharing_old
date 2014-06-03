package com.soloway.city.milesharing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.gson.Gson;
import com.soloway.city.milesharing.api.UserProfile;
import com.soloway.city.milesharing.api.UserSession;
import com.soloway.city.milesharing.api.UsersHelper;
import com.soloway.city.milesharing.fragments.NotifyFragment;
import com.soloway.city.milesharing.fragments.PassDriveFragment;
import com.soloway.city.milesharing.routing.GMapV2GetRouteDirection;
import com.soloway.transport.milesharing.R;

public class MainMapActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener, 
		/*OnClickListener,*/ 
		GooglePlayServicesClient.ConnectionCallbacks,
		/*OnMapClickListener,*/ OnMapLongClickListener /*,
		UserLoginDialogListener*/ {

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
  	     
  	      R.drawable.history_icon,
  	      R.drawable.route_icon,
  	      R.drawable.people_icon,
  	      R.drawable.preferences_icon
  	  
  	  };
    
    Integer[] imageId_d = {
    	      R.drawable.driver_icon,
    	     
    	      R.drawable.history_icon,
      	      R.drawable.route_icon,
      	      R.drawable.people_icon,
      	      R.drawable.preferences_icon
    	  
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
    LocationManager locationManager;
    LocationClient mLocationClient;
    
    SharedPreferences  prefs;
    UserSession session;
    
    private String[] drawerListViewItems;
    private ListView drawerListView;
    
    private MyApplication myApp;
    
    private Marker marker;
    private LinearLayout layout;
    private LinearLayout layout_top;
    private TextView tvDis;
    private TextView tvDur;
    
    private MainMapActivity mainMapActivity;
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
		
		myApp = (MyApplication) getApplicationContext();
		// MAP STUFF
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
 
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
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Last Location
            Location location = locationManager.getLastKnownLocation(provider);
 
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
            
            /*drawerListViewItems = getResources().getStringArray(R.array.items_pass);
            
            // get ListView defined in activity_main.xml
            drawerListView = (ListView) findViewById(R.id.choose_item);
     
            adapter = new CustomList(MainMapActivity.this, drawerListViewItems, imageId_p);
            drawerListView.setAdapter(adapter);*/

            markerOptions = new MarkerOptions();
            
            if (location!=null){
            	fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
            } else{
            	mLocationClient = new LocationClient(this.getBaseContext(),null,null);//
            	// Connect the client.
                mLocationClient.connect();
                location = mLocationClient.getLastLocation();
            	fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
            	//mLocationClient.disconnect();
            }
            
            LatLng ln = new LatLng(location.getLatitude(),location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ln));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            
            
            ///MY PART
            
            prefs = this.getSharedPreferences("com.soloway.transport.milesharing", Context.MODE_PRIVATE);
            
            
          
          	        	  
            
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
	        
	        fromPosition = latLng;
	 
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
	    
	    private void showNotifyDialog(){
	    	FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragMan.beginTransaction();
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_layout);
            Fragment myFrag = new NotifyFragment();
            fragTransaction.add(rl.getId(), myFrag , "fragmentNotify");
            fragTransaction.commit();
	    }
	    
	    private void showRole(){
	    	RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_layout);
            Fragment fragPD = new PassDriveFragment();
            getSupportFragmentManager().beginTransaction().add(rl.getId(),fragPD,"fragmentPassDriver").commit();
	    }
	    
	    @Override
		public void onMapLongClick(LatLng point) {
			//googleMap.addMarker(new MarkerOptions().position(point).title("Go"));
			
	    	
			
	        
	        toPosition = new LatLng(point.latitude, point.longitude);
	        googleMap.clear();	
	        GetRouteTask getRoute = new GetRouteTask();
	        getRoute.execute();
	        
	        if (marker != null) {
				marker.remove();
	        }
			
	        
	        
	        marker = googleMap.addMarker(new MarkerOptions()
	                .position(point).title("Go")
	                .draggable(true).visible(true));
	       
	        layout = (LinearLayout) findViewById(R.id.bottom_box);
	        
	        //showNotifyDialog();
            //�������� ������ ����
	        //showRole();
	        
	        }
	    
	 
	    
	    public void showLoginDialog(final boolean first_attempt, final boolean reg) {
	        
	        final AlertDialog.Builder loginRegDialog = new AlertDialog.Builder(this);
	        mainMapActivity = this;
	        
	        loginRegDialog.setIcon((reg)?R.drawable.ic_user_create:R.drawable.ic_user_login);
	        loginRegDialog.setTitle((reg)?R.string.user_register_title:R.string.user_login_title);

	        View linearlayout = getLayoutInflater().inflate(R.layout.fragment_user_login, null);
	        loginRegDialog.setView(linearlayout); 
	        
	        final EditText loginEdit = (EditText) linearlayout.findViewById(R.id.txt_your_login);
	        final EditText passEdit = (EditText) linearlayout.findViewById(R.id.txt_your_pass);
	        final TextView errorTextView = (TextView) linearlayout.findViewById(R.id.lbl_login_error);
	        final LinearLayout signUpLayout = (LinearLayout) linearlayout.findViewById(R.id.new_user_call);
	        final LinearLayout newUserData = (LinearLayout) linearlayout.findViewById(R.id.new_user_data);
	        final Button registerEnable = (Button) linearlayout.findViewById(R.id.newUserCall);
	        errorTextView.setTextColor(Color.parseColor("#FF3322"));
	        errorTextView.setVisibility(View.GONE);
	        signUpLayout.setVisibility(View.GONE);
	        newUserData.setVisibility((reg)?View.VISIBLE:View.GONE);
	        if (!first_attempt && reg){
	        	passEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	        }
	        
	        
	        registerEnable.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loginRegDialog.setIcon(R.drawable.ic_user_create);
	        		loginRegDialog.setTitle(R.string.user_register_title);
	        		
	        		
					Animation animation = new TranslateAnimation(0,0,0,1000);
	        		animation.setDuration(1000);
	        		errorTextView.startAnimation(animation);
	        		 signUpLayout.startAnimation(animation);
	        		 newUserData.startAnimation(animation);
	        		errorTextView.setVisibility(View.GONE);
	        		 signUpLayout.setVisibility(View.GONE);
	        		 newUserData.setVisibility(View.VISIBLE);
	        		 
	        		 myApp.viewToClick.performClick();
	        		 mainMapActivity.showLoginDialog(false, true);
	        		 
				}
			});
	        
	        if (!first_attempt && !reg){
	        	Animation animation = new TranslateAnimation(0,0,0,1000);
        		animation.setDuration(1000);
        		errorTextView.startAnimation(animation);
        		 signUpLayout.startAnimation(animation);
        		errorTextView.setVisibility(View.VISIBLE);
        		 signUpLayout.setVisibility(View.VISIBLE);
	        }
	        
	        loginRegDialog.setPositiveButton((reg)?R.string.register:R.string.login,
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	                    	UserProfile authUser = new UserProfile();
	                    	if (reg){
	                    		//����������� ������������
	                    		EditText firstNameEdit = (EditText) newUserData.findViewById(R.id.txt_first_name);
	                    		authUser.setFirstName(firstNameEdit.getText().toString());
	                    		EditText lastNameEdit = (EditText) newUserData.findViewById(R.id.txt_last_name);
	                    		authUser.setSecondName(lastNameEdit.getText().toString());
	                    		
	                    		EditText emailEdit = (EditText) newUserData.findViewById(R.id.txt_email);
	                    		authUser.setEmail(emailEdit.getText().toString());
	                    		
	                    	}
	                    	authUser.setUserLogin(loginEdit.getText().toString());
	                    	
	                    	String hashPass =  passEdit.getText().toString();
	                    	MessageDigest md;
							try {
								md = MessageDigest.getInstance("MD5");
								md.update(passEdit.getText().toString().getBytes());

		                        byte[] digest = md.digest();
		                        StringBuilder sb = new StringBuilder();
		                        for (int i = 0; i < digest.length; i++) {
		                            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
		                        }

		                        hashPass =  sb.toString();
		                        
							} catch (NoSuchAlgorithmException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
	                    	
	                    	authUser.setUserPassword(hashPass);
	                    	
	                    	
	                    	UserSession tmpSession = null;
	                    	
	                    	if (reg){
	                    		tmpSession = UsersHelper.pushUser(authUser);
	                    	} else{
	                    		tmpSession = UsersHelper.authUser(authUser);
	                    	}
	                    	
	                    	if (tmpSession.isOnline()){
	                    		if (errorTextView.getVisibility() == View.VISIBLE){
	                    			Animation animation = new TranslateAnimation(0,0,0,1000);
		                    		animation.setDuration(1000);
		                    		errorTextView.startAnimation(animation);
		                    		errorTextView.setVisibility(View.GONE);
	                    		}
	                    		
	                    		session.setSessionId(tmpSession.getSessionId());
	                    		session.setTokenId(tmpSession.getTokenId());
	                    		session.setUserId(tmpSession.getUserId());
	                    		
	                    		session.setOnline(tmpSession.isOnline());
	                    		
	                    		dialog.dismiss();
	                    		
	                    	} else {

	                    		mainMapActivity.showLoginDialog(false, false);

	                    	}
	                    	
	                        
	                    }
	                })

	                .setNegativeButton(R.string.cancel,
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                                dialog.cancel();
	                            }
	                        });

	        loginRegDialog.create();
	        AlertDialog dlg = loginRegDialog.show();
	        

	        myApp.viewToClick = dlg.getButton(DialogInterface.BUTTON_NEGATIVE);
	        
	       
	    }
	    
	    private ProgressDialog dialog;
	    
	    class LoginRequestTask extends AsyncTask<UserProfile, String, String> {

            @Override
            protected String doInBackground(UserProfile... params) {

                    try {
                    	//
                            //������� ������ �� ������
                            DefaultHttpClient hc = new DefaultHttpClient();
                            ResponseHandler<String> res = new BasicResponseHandler();
                            //�� � ��� ����� �������� post ������
                            HttpPost postMethod = new HttpPost("http://78.47.251.3/users.php?push_user"+params[0].getRegData());
                            //����� ���������� ��� ���������
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            //�������� ��������� �� ����� �����������
                            //�����
//                            nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString()));?
                            //������
//                            nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
                           
                            
                            //�������� �� ������ � �������� �� ������
//                            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            //�������� ����� �� �������
                            String response = hc.execute(postMethod, res);
                            
                    } catch (Exception e) {
                            System.out.println("Exp=" + e);
                    }
                    return null;
            }

            @Override
            protected void onPostExecute(String result) {

                    dialog.dismiss();
                    super.onPostExecute(result);
            }

            @Override
            protected void onPreExecute() {

                    dialog = new ProgressDialog(MainMapActivity.this);
                    dialog.setMessage("����������...");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(true);
                    dialog.show();
                    super.onPreExecute();
            }
	    }
	    
	// ROUTE LOAD SPLASH
	    private class GetRouteTask extends AsyncTask<String, Void, String> {
	        
	    	private void showLogin(){
	    		if (prefs != null)
               	{
               		Gson gson = new Gson();
               	    String json = prefs.getString("UserSession", "");
               	    session = gson.fromJson(json, UserSession.class);
               	    
               	    if (session != null){
               	    	//attempt to login or register
               	    	boolean first_attempt = true;
               	    	showLoginDialog(first_attempt, false);
               	    	
               	    } else {
               	    	// nice. is it the first launch? Ok. let's create session!
               	    	session = new UserSession();
               	    	Editor prefsEditor = prefs.edit();
               	        Gson gsonBuffer = new Gson();
               	        String jsonBuffer = gsonBuffer.toJson(session);
               	        prefsEditor.putString("UserSession", jsonBuffer);
               	        prefsEditor.commit();
               	        
               	        // cool. so, it's time to login or register. isn't it?
               	        boolean first_attempt = true;
               	        showLoginDialog(first_attempt, false);
               	    }
               	    

               	}
	    	}
	    	
	        private ProgressDialog Dialog;
	        String response = "";
	        @Override
	        protected void onPreExecute() {
	              Dialog = new ProgressDialog(MainMapActivity.this);
	              Dialog.setMessage("����� ��������...");
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
	              //googleMap.clear();
	              if(response.equalsIgnoreCase("Success")){
	            	  
	             dur =  v2GetRouteDirection.getDurationValue(document);
	             dis  =  v2GetRouteDirection.getDistanceValue(document);
	              
	             
	             googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

	                 // Use default InfoWindow frame
	                 @Override
	                 public View getInfoWindow(Marker arg0) {
	                     return null;
	                 }

	                 // Defines the contents of the InfoWindow
	                 @Override
	                 public View getInfoContents(Marker arg0) {

	                     // Getting view from the layout file info_window_layout
	                     View v = getLayoutInflater().inflate(R.layout.info_window, null);

	                     // Getting the position from the marker
	                     LatLng latLng = arg0.getPosition();

	                     //Finding buttons\
	                     //Button bGo = ((Button) v.findViewById(R.id.btn_ok));
	                     //Button bCancel = ((Button) v.findViewById(R.id.btn_cancel));
	                      
	                     
	                     tvDis = (TextView) v.findViewById(R.id.textLength);
	                     tvDur = (TextView) v.findViewById(R.id.textTime);
	                    // tvDis.setText("����������: "+String.valueOf(dis));
	                    // tvDur.setText("�����: "+String.valueOf(dur));
	                     tvDis.setText("����������: "+String.valueOf((float) dis/1000)+ " ��.");
	                     tvDur.setText("�����: "+String.valueOf((int) dur/60)+" ���.");
	                     
	                     //bGo.setText("dgfd");
	                   
	    	            noBtns = false;
	                     
	                     return v;

	                 }
	             });
	             
	             marker.showInfoWindow();
	             
	             
	             googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
	   	        public void onInfoWindowClick(Marker marker){
	   	        			showRole();
	                     }
	                   });
	             
	             
	             
                 
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
	              //markerOptions.position(toPosition);
	             // markerOptions.draggable(true);
	              //googleMap.addMarker(markerOptions);

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
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
		location = mLocationClient.getLastLocation();
    	fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
    	mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStart(){
		if (mLocationClient != null){
			mLocationClient.connect();
		}
		super.onStart();
	}
	
	@Override
	public void onStop(){
		 // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            mLocationClient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
	}

}
