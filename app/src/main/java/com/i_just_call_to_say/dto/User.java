package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="User")
public class User implements Serializable{
	public String getPurchase_status() {
		return purchase_status;
	}
	public void setPurchase_status(String purchase_status) {
		this.purchase_status = purchase_status;
	}
	@SerializedName("user_id")
	@DatabaseField(id = true,columnName="user_ID")
	private int user_id;
	
	@SerializedName("access_token")
	@DatabaseField(columnName="Access_Token")
	private String access_token;
	
	@SerializedName("device_token")
	private String deviceToken;
	
	@SerializedName("name")
	@DatabaseField(columnName="User_Name")
	private String user_name;
	
	@SerializedName("country_code")
	@DatabaseField(columnName="country_code")
	private String country_code;
		
	@SerializedName("phone_number")
	@DatabaseField(columnName="Phone_Number")
	private String phone_number;
	
	
	
	
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	@SerializedName("subscription_status")
	@DatabaseField(columnName="Purchase_Status")
	private String purchase_status="false";
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

}
