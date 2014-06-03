package com.soloway.city.milesharing.old;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.soloway.city.milesharing.MainMapActivity;
import com.soloway.city.milesharing.api.UserProfile;

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
//                    nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString()));?
                    //������
//                    nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
                   
                    
                    //�������� �� ������ � �������� �� ������
//                    postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //�������� ����� �� �������
                    String response = hc.execute(postMethod, res);
                    
            } catch (Exception e) {
                    System.out.println("Exp=" + e);
            }
            return null;
    }

    @Override
    protected void onPostExecute(String result) {
  	  	   // uncomment this to use as inner class in MainMapActivity
           // dialog.dismiss();
            super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {

    	  // uncomment this to use as inner class in MainMapActivity
          //  dialog = new ProgressDialog(MainMapActivity.this);
          //  dialog.setMessage("����������...");
          //  dialog.setIndeterminate(true);
          //  dialog.setCancelable(true);
          //  dialog.show();
            super.onPreExecute();
    }
}