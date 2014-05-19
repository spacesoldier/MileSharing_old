package com.soloway.city.milesharing.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class UsersHelper {
	
	public static UserSession pushUser(final UserProfile profile){
		
		final UserSession result = new UserSession();
		
		new Thread( new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://78.47.251.3/users.php?push_user"+profile.getRegData());
                

                // Making HTTP Request
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    if (response.toString().equalsIgnoreCase("OK")){
                    	result.setOnline(true);
                    }
                    result.setOnline(true);
                    
                } catch (ClientProtocolException e) {
                    // writing exception to log
                    e.printStackTrace();
                } catch (IOException e) {
                    // writing exception to log
                    e.printStackTrace();

                }

            }
        }).start();
		
		return result;
	}
	
	public static UserSession authUser(final UserProfile profile){
		
		final UserSession result = new UserSession();
		
		new Thread( new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                //HttpPost httpPost = new HttpPost("http://78.47.251.3/users.php?auth_user"+profile.getAuthData());
                
                 String urlRequest = "http://1.soloway-milesharing.appspot.com/milesharingbackend/locate";
                 urlRequest = urlRequest+"?login="+String.valueOf(profile.getUserLogin());
                 urlRequest = urlRequest+"&pass="+String.valueOf(profile.getUserPassword());
                 HttpPost httpPost = new HttpPost(urlRequest);
                 
                /* 
                 * 19.05.14 ��������
                HttpPost httpPost = new HttpPost("http://1.soloway-milesharing.appspot.com/milesharingbackend/locate");
               
                HttpParams params = httpPost.getParams();
                params.setParameter("login", profile.getUserLogin());
                params.setParameter("pass", profile.getUserPassword());
                httpPost.setParams(params);
                */

                // Execute HTTP Post Request
                // Making HTTP Request
                try {
                	
                	/*
                	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("login", profile.getUserLogin()));
                    nameValuePairs.add(new BasicNameValuePair("pass", profile.getUserPassword()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    
                    */
                	HttpResponse response = httpClient.execute(httpPost);
                    	String result1 = EntityUtils.toString(response.getEntity());
                    	
                    	result.setOnline(true);
                    
                    // writing response to log
//                    Log.d("Http Response:", response.toString());
                } catch (ClientProtocolException e) {
                    // writing exception to log
                    e.printStackTrace();
                } catch (IOException e) {
                    // writing exception to log
                    e.printStackTrace();

                }

            }
        }).start();
		
		return result;
	}
	
	public static UserSession exitUser(UserProfile profile){
		
		new Thread( new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://78.47.251.3/users.php?exit");
                

                // Making HTTP Request
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    if (response.toString().equalsIgnoreCase("OK")){
                    	
                    }
                    // writing response to log
//                    Log.d("Http Response:", response.toString());
                } catch (ClientProtocolException e) {
                    // writing exception to log
                    e.printStackTrace();
                } catch (IOException e) {
                    // writing exception to log
                    e.printStackTrace();

                }

            }
        }).start();
		
		return new UserSession();
	}
	
	
}
