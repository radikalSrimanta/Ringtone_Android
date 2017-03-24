package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CardCategory implements Serializable{
	
	@SerializedName("category_id")
	private String category_id;
	
	
	@SerializedName("category_name")
	private String category_name;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

}
