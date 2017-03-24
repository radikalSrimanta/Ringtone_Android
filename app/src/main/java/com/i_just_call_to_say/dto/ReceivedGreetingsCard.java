package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ReceivedGreetingsCard extends GreetingsCard implements	Serializable {

	@SerializedName("card_send_time")
	private String sending_time;

	@SerializedName("sender_phone_no")
	private String sender_phone_no = "";
	
	public String getSending_time() {
		return sending_time;
	}

	public void setSending_time(String sending_time) {
		this.sending_time = sending_time;
	}

	public String getSender_phone_no() {
		return sender_phone_no;
	}

	public void setSender_phone_no(String sender_phone_no) {
		this.sender_phone_no = sender_phone_no;
	}

}
