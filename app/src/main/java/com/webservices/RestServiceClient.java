package com.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.AppConstant;
import com.i_just_call_to_say.activities.splash.SplashActivity;
import com.utility.CommonUtility;
import com.webservices.interfaces.IServerResponse;


public class RestServiceClient extends AsyncTask<Void, Void, String> {

	//private ProgressDialog pd;
	private ProgressDialog pd;
	private IServerResponse iResponse;
	private Object[] values;
	private String filePath = "";
	private String keyForUploadFile = "";
	private String[] keys;
	private String url;
	private String[] filepathlist, keyforuploadlist;
	private Context context;
	private static final int ConnectionTimeout = 90 * 1000;
	private String serverErrorMessage = "Some Server Problem has been encountered";
	
	private String loadingMessage = "Loading...";

	public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Context context) {
		this.iResponse = iResponse;
		this.values = values;
		this.keys = keys;
		this.url = url;
		this.context=context;
	}
	
	public RestServiceClient(IServerResponse iResponse, Object[] values,
			String[] keys, String url, Context context,	String keyForUploadFile, String filePath) {
		this.iResponse = iResponse;
		this.values = values;
		this.keys = keys;
		this.url = url;
		this.context=context;
		this.filePath = filePath;
		this.keyForUploadFile = keyForUploadFile;
	}
	
	public RestServiceClient(IServerResponse iResponse, Object[] values,
			String[] keys, String url,Context context, String keyForUploadFile, String filePath,String lodingMessage) {
		this.iResponse = iResponse;
		this.values = values;
		this.keys = keys;
		this.url = url;
		this.context=context;
		this.filePath = filePath;
		this.keyForUploadFile = keyForUploadFile;
		this.loadingMessage = lodingMessage;
	}
	
	public RestServiceClient(IServerResponse iResponse, Object[] values,
			String[] keys, String url,Context context, String[] keyForUploadFile, String[] filePath,String lodingMessage) {
		this.iResponse = iResponse;
		this.values = values;
		this.keys = keys;
		this.url = url;
		this.context=context;
		this.filepathlist = filePath;
		this.keyforuploadlist = keyForUploadFile;
		this.loadingMessage = lodingMessage;
	}
	
	public RestServiceClient(IServerResponse iResponse, Object[] values,
			String[] keys, String url, Context context,String loadingMessage) {
		this.iResponse = iResponse;
		this.values = values;
		this.keys = keys;
		this.url = url;
		this.context=context;
		this.loadingMessage = loadingMessage;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		//System.out.println(((Activity)context).isFinishing()+"    context "+context);
		
		if (!"".equalsIgnoreCase(loadingMessage) && !((Activity)context).isFinishing()) {
		   /* pd = DialogUtility.getCustonProgreeBar(context, loadingMessage);
			 * if(!pd.isShowing()){
			 * System.out.println("is showing "+pd.isShowing()); pd.show(); }
			 */
			try {
				if (context instanceof SplashActivity) {
					
				} else {
					pd = new ProgressDialog(context,R.style.alertDialogTheme);
					pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pd.setCancelable(false);
					pd.setMessage(loadingMessage);
					pd.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String sResponse = null;
		StringBuilder builderResponse = new StringBuilder();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ConnectionTimeout);
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, ConnectionTimeout);
			HttpPost postRequest = new HttpPost(url);
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (values != null && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					Log.e("Data ", keys[i] + " " + values[i]);
					if (values[i] instanceof String) {
						reqEntity.addPart(keys[i],
								new StringBody(values[i].toString()));
					} else if (values[i] instanceof Integer) {
						reqEntity.addPart(keys[i],
								new StringBody(String.valueOf(values[i])));
					}
				}
				if (keyforuploadlist != null && keyforuploadlist.length > 0) {
					for (int i = 0; i < keyforuploadlist.length; i++) {
						addFilesInRequestEntity(reqEntity, keyforuploadlist[i], filepathlist[i]);
					}
				} else {
					addFilesInRequestEntity(reqEntity, keyForUploadFile, filePath);
				}
				postRequest.setEntity(reqEntity);
				Log.e("Data", "Request " + postRequest.getURI());
			}
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

			while ((sResponse = reader.readLine()) != null) {
				builderResponse = builderResponse.append(sResponse);
			}
			Log.e("responce ","send card");
			System.out.println("Data Response111: " + builderResponse + "  Status Code: " + response.getStatusLine().getStatusCode());

		} catch (Exception e){
			e.printStackTrace();
//			serverErrorMessage = e.getMessage();
			serverErrorMessage = AppConstant.SERVER_ERROR_MESSAGE;
		} 
		
		return builderResponse.toString();
	}

	
	
	
	@Override
	protected void onPostExecute(String sResponse) {
		//sResponse = removeNullValues(sResponse);
		System.out.println(" context "+context);
		try {
			if (context != null) {
				if (context instanceof SplashActivity) {				
				}
				else
				if (pd != null) {
					pd.dismiss();
				}
				if (iResponse != null && !sResponse.equals("")) {
					JSONObject json;
					String errorCode = "1";
					try {
						json = new JSONObject(sResponse);
						errorCode = json.optString("code");
						String msg = json.optString("status");
						Log.e("errorcode", errorCode);
						Log.e("msg", msg);
						if (errorCode.equals("200")) {
							System.out.println("send response ");
							iResponse.onSuccess(sResponse);
						} else {
							iResponse.onFailure(msg, errorCode);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						iResponse.onFailure(serverErrorMessage, "1");
					}
				} else {
					iResponse.onFailure(serverErrorMessage, "1");
				}
			}
		} catch (Exception e) {
			iResponse.onFailure(serverErrorMessage, "1");
			e.printStackTrace();
		}
	}
	
	private void addFilesInRequestEntity(MultipartEntity reqEntity,String keyForUploadFile, String filePath) {
		System.out.println("RestClient: FilePath " + filePath);
		System.out.println("RestClient: keyForUploadFile " + keyForUploadFile);
		if(filePath != null && !"".equals(filePath)){
			File file = new File(filePath);
			//System.out.println("RestClient: File " + file);
			if(file != null){
				FileBody filebodyVideo = new FileBody(file);
				reqEntity.addPart(keyForUploadFile, filebodyVideo);
			}
		}

	}
	
	
	private String removeNullValues(String responseValues){
		String tmp_response = responseValues.replace("null", " ");
		tmp_response = responseValues.replace("(", " ");
		tmp_response = responseValues.replace(")", " ");
		return tmp_response;
	}

}
