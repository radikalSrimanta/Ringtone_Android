package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.ContactStatusDto;
import com.i_just_call_to_say.dto.Contacts;

public class ContactStatusWrapper implements Serializable {

	public List<ContactStatusDto> getContact_list() {
		return contact_list;
	}

	public void setContact_list(List<ContactStatusDto> contact_list) {
		this.contact_list = contact_list;
	}

	@SerializedName("result")
	private List<ContactStatusDto> contact_list;
}
