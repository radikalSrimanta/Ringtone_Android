package com.i_just_call_to_say.dto.wrapper;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.i_just_call_to_say.dto.GreetingsCard;

public class GreetingsCardDTOWrapper implements Serializable{
	
	
	@SerializedName("result")
	private List<GreetingsCard> greetingscardlist;

	public List<GreetingsCard> getGreetingscardlist() {
		return greetingscardlist;
	}

	public void setGreetingscardlist(List<GreetingsCard> greetingscardlist) {
		this.greetingscardlist = greetingscardlist;
	}

	@Override
	public String toString() {
		return "GreetingsCardDTOWrapper [greetingscardlist="
				+ greetingscardlist + "]";
	}
	
	

}
