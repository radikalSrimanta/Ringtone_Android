package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;


public class CategoryDTOWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 123455656L;


	@SerializedName("result")
	private List<CardCategory> category_list;


	@Override
	public String toString() {
		return "ContactsDTOWrapper [contact_list=" + category_list + "]";
	}


	public List<CardCategory> getCategory_list() {
		return category_list;
	}


	public void setCategory_list(List<CardCategory> category_list) {
		this.category_list = category_list;
	}


	


	

}
