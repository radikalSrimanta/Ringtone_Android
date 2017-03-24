package com.webservices.interfaces;

public interface IServerResponse {
	
	public void onSuccess(String response);

	public void onFailure(String message,String errorcode);
}
