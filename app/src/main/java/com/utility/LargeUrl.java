package com.utility;

import org.json.JSONException;
import org.json.JSONObject;

import com.utility.URLShortener.URLShortenerListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LargeUrl extends AsyncTask<String, String, JSONObject>{

	private ProgressDialog pDialog;
    private String shortUrl;
	private Activity mActivity;
	private URLShortenerListener urlShortenerListener;
    
    public LargeUrl (Activity activity , String shortUrl,URLShortenerListener urlShortenerListener){
    	this.mActivity = activity;
    	this.shortUrl = shortUrl;
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
    JSONObject json = jParser.getLongUrl(shortUrl);
    return json;
  }
  String ShortUrl;
   @Override
     protected void onPostExecute(JSONObject json) {
//     pDialog.dismiss();
     try {
        if (json != null){
	        ShortUrl = json.getString("longUrl");
        }else{
        	ShortUrl = shortUrl;
        }
        urlShortenerListener.onComplete(ShortUrl);
//        pDialog.dismiss();
    } catch (JSONException e) {
      e.printStackTrace();
    }
   }

}
