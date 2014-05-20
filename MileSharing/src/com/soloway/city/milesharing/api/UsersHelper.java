package com.soloway.city.milesharing.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.google.gson.Gson;
import com.soloway.city.milesharing.backend.messages.LoginUserRequest;
import com.soloway.city.milesharing.backend.messages.LoginUserResponse;
import com.soloway.city.milesharing.backend.messages.NewUserRequest;
import com.soloway.city.milesharing.backend.messages.NewUserResponse;

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
                
                /*
                 String urlRequest = "http://1.soloway-milesharing.appspot.com/milesharingbackend/locate";
                 urlRequest = urlRequest+"?login="+String.valueOf(profile.getUserLogin());
                 urlRequest = urlRequest+"&pass="+String.valueOf(profile.getUserPassword());
                 HttpPost httpPost = new HttpPost(urlRequest);
                 */
                
                 String urlLogin="http://1.soloway-milesharing.appspot.com/milesharingbackend/login_user";
                 LoginUserRequest regRequest = new LoginUserRequest();
                 regRequest.setLogin(profile.getUserLogin());
                 regRequest.setPass(profile.getUserPassword());
                 Gson gson = new Gson();
                 String regResultStr = gson.toJson(regRequest);
                 
                 try {
					regResultStr = URLEncoder.encode(regResultStr, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
                 urlLogin = urlLogin +"?data="+regResultStr;
                
                 /*
                 urlLogin= urlLogin.replace("\"","%22");
                 urlLogin= urlLogin.replace("{","%7B");
                 urlLogin= urlLogin.replace(":","%3A");
                 urlLogin= urlLogin.replace(",","%2C");
                 urlLogin= urlLogin.replace("}","%7D");
                 */
                 HttpPost httpPost = new HttpPost(urlLogin);
              
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
                    	
                	Gson gson1 = new Gson();
                	LoginUserResponse newRecResp = gson.fromJson(result1, LoginUserResponse.class);
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
