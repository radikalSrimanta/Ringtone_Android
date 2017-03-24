package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.table.DatabaseTable;



public class GreetingsCard implements Serializable{
	
	
	@SerializedName("card_id")
	private String card_id;
	
	@SerializedName("card_name")//343434341233
	private String card_name;
	
	@SerializedName("card_image_url")
	private String card_image_url;
	
	@SerializedName("card_imagethumbnail_url")
	private String card_imagethumbnail_url;
	
	@SerializedName("is_free")
	private String is_free;
	
	
	

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_image_url() {
		return card_image_url;
	}

	public void setCard_image_url(String card_image_url) {
		this.card_image_url = card_image_url;
	}

	public String getCard_imagethumbnail_url() {
		return card_imagethumbnail_url;
	}

	public void setCard_imagethumbnail_url(String card_imagethumbnail_url) {
		this.card_imagethumbnail_url = card_imagethumbnail_url;
	}

	public String getIs_free() {
		return is_free;
	}

	public void setIs_free(String is_free) {
		this.is_free = is_free;
	}

}
