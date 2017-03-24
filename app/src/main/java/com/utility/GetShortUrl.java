package com.utility;

import org.json.JSONException;
import org.json.JSONObject;

import com.utility.URLShortener.URLShortenerListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetShortUrl extends AsyncTask<String, String, JSONObject>{

	private ProgressDialog pDialog;
    private String longUrl;
	private Activity mActivity;
	private URLShortenerListener urlShortenerListener;
    
    public GetShortUrl (Activity activity , String longUrl, URLShortenerListener urlShortenerListener){
    	this.mActivity = activity;
    	this.longUrl = longUrl;
    	this.urlShortenerListener = urlShortenerListener;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pDialog = new ProgressDialog(mActivity);
//        pDialog.setMessage("Contacting Google Servers ...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(true);
//        pDialog.show();
  }
  @Override
    protected JSONObject doInBackground(String... args) {
    URLShortener jParser = new URLShortener();
    JSONObject json = jParser.getShortUrl(longUrl);
    return json;
  }
  String ShortUrl;
   @Override
     protected void onPostExecute(JSONObject json) {
//     pDialog.dismiss();
     try {
        if (json != null){
	        ShortUrl = json.getString("id");
        }else{
        	ShortUrl= longUrl;
        }
        System.out.println("ShortUrl "+ShortUrl);
        urlShortenerListener.onComplete(ShortUrl);
//        pDialog.dismiss();
    } catch (JSONException e) {
      e.printStackTrace();
    }
   }

}
