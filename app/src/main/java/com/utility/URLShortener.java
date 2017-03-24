package com.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class URLShortener {
	 static InputStream is = null;
	 static JSONObject jObj = null;//https://developers.google.com/url-shortener/v1/getting_started
	 static String json = "";
	 
	 
	 public static String LongToShortUrl = "https://www.googleapis.com/urlshortener/v1/url?";
	 public static String ShortToLongUrl = "https://www.googleapis.com/urlshortener/v1/url?shortUrl=";
	    
	    public URLShortener() {
	    }
	    
	    public JSONObject getShortUrl(String longUrl) {
	        // Making HTTP request
	        try {
	            // DefaultHttpClient
	        	System.out.println("url-->"+LongToShortUrl+longUrl);
	        	
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(LongToShortUrl);
	            System.out.println("long url-->"+"{\"longUrl\":\""+longUrl+"\"}");
	            
	            httpPost.setEntity(new StringEntity("{\"longUrl\":\""+longUrl+"\"}"));
	            httpPost.setHeader("Content-Type", "application/json");
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	            Log.e("JSON", json);
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	        // Parse the String to a JSON Object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	        // Return JSON String
	        return jObj;
	    }
	    
	    
	    public JSONObject getLongUrl(String shortUrl) {
	        // Making HTTP request
	        try {
	            // DefaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(ShortToLongUrl+shortUrl);
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	            Log.e("JSON", json);
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	        // Parse the String to a JSON Object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	        // Return JSON String
	        return jObj;
	    }
	    
	    public interface URLShortenerListener{
	    	public void onComplete(String url);
	    }
}
