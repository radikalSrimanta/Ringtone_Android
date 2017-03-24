package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.User;


public class UserDTOWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 123455656L;


	@SerializedName("result")
	private User user;


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UserDtoWrapper [user="
		+ user + "]";
	}

}
