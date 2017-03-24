package com.webservices;

import com.i_just_call_to_say.activities.base.AppConstant;
import com.utility.CommonUtility;
import com.utility.CustomDialogUtility;
import com.utility.DialogUtility;
import com.utility.constants.UrlConstants;
import com.webservices.interfaces.IRequestCaller;
import com.webservices.interfaces.IServerResponse;

import android.content.Context;

public class TaskManager implements IServerResponse{

	private IRequestCaller iRequestCaller;
	private IServerResponse iServerResponse;
	
	private Context context;

	@Override
	public void onSuccess(String response) {
		iServerResponse.onSuccess(response);
	}

	@Override
	public void onFailure(String message,String errorCode) {
		iServerResponse.onFailure(message,errorCode);
	}
	
	
	
	

	/*public TaskManager(IRequestCaller caller,IServerResponse iServerResponse, Activity mActivity) {
		iRequestCaller = caller;
		this.iServerResponse = iServerResponse;
		activity = mActivity;
	}*/
	
	public TaskManager(IRequestCaller caller,IServerResponse iServerResponse, Context context) {
		iRequestCaller = caller;
		this.iServerResponse = iServerResponse;
		this.context=context;
	}

	public void callService() {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this,
					values, keys, url, context);
			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}
	
	public void callServiceContext() {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this,values, keys, url, context,"");
			restServiceClient.execute();
		} /*else {
			DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.No internet connection!), null);
		}*/
	}
	
	public void callService(String loadingMessage) {
		
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this,
					values, keys, url, context,loadingMessage);
			System.out.println("user login57"+url);
			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}
	
	public void callService(String keyForFileUpload,String Path) {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, context, keyForFileUpload, Path);
			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}
	

	public void callService(String keyForFileUpload,String Path,String loadingmessage) {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this,
					values, keys, url, context, keyForFileUpload, Path, loadingmessage);
			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}
	
	public void callService(String[] keyForFileUpload,String[] Path,String loadingmessage) {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, context,keyForFileUpload,Path,loadingmessage);

			System.out.println("updatted"+values.toString());
			System.out.println("updatted"+keys.toString());
			System.out.println("updatted"+url);

			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}
	
	public void callService_msgboard(String loadingmessage,String Path,String keyForFileUpload) {
		if (CommonUtility.checkConnectivity(context)) {

			String[] keys = iRequestCaller.getKeys();
			Object[] values = iRequestCaller.getValues();
			String url = UrlConstants.BASE_URL + iRequestCaller.getWebServiceMethod();
			RestServiceClient restServiceClient = new RestServiceClient(this,
					values, keys, url, context,keyForFileUpload,Path,loadingmessage);
			restServiceClient.execute();
		} else {
			CustomDialogUtility.showMessageWithOk(AppConstant.SERVER_ERROR_MESSAGE, context);
		}
	}

	
	
}