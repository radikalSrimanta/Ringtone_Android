package com.isis.module.push_notification.interfaces;

public interface IPushResponce {

	public void gotoNextActivity();
	public void sendTokenToServer(String deviceToken,String deviceID);
}
