package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;

public class ReceivedGreetingDTOWrapper implements Serializable{
	
	

	@SerializedName("result")
	private List<ReceivedGreetingsCard> greetingscardlist;

	
	public List<ReceivedGreetingsCard> getGreetingscardlist() {
		return greetingscardlist;
	}

	public void setGreetingscardlist(List<ReceivedGreetingsCard> greetingscardlist) {
		this.greetingscardlist = greetingscardlist;
	}

	

}
