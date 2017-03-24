package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class ContactStatusDto implements Serializable {

	@SerializedName("phone_number")
	private String contact_number="";
	
	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getConnection_status() {
		return connection_status;
	}

	public void setConnection_status(String connection_status) {
		this.connection_status = connection_status;
	}

	@SerializedName("connection_status")
	private String connection_status="NC";
	
	@SerializedName("thumbnailpic_senderEnd_url")
	private String my_image = "";

	public String getMy_image() {
		return my_image;
	}

	public void setMy_image(String my_image) {
		this.my_image = my_image;
	}
}
