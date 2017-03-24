package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.User;


public class ContactsDTOWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 123455656L;


	@SerializedName("result")
	private List<Contacts> contact_list;


	@Override
	public String toString() {
		return "ContactsDTOWrapper [contact_list=" + contact_list + "]";
	}


	public List<Contacts> getContact_list() {
		return contact_list;
	}


	public void setContact_list(List<Contacts> contact_list) {
		this.contact_list = contact_list;
	}


	

}
