package com.i_just_call_to_say.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName="Contacts")
public class Contacts implements Serializable{

	
	private String contact_type = "";
	
	private String raw_contact_id = "";
	
	public String getRaw_contact_id() {
		return raw_contact_id;
	}

	public void setRaw_contact_id(String raw_contact_id) {
		this.raw_contact_id = raw_contact_id;
	}

	public String getContact_type() {
		return contact_type;
	}
	
	public void setContact_type(String contact_type) {
		this.contact_type = contact_type;
	}

	@SerializedName("phone_number")
	@DatabaseField(id=true,columnName="ContactNumber")
	private String contact_number="";

	@SerializedName("contact_id")
	@DatabaseField(columnName="ContactsID")
	private String contact_id="";

	@SerializedName("connections_name")
	@DatabaseField(columnName="ContactName")
	private String contact_name="";

	@SerializedName("thumbnailpic_receiverEnd_url")
	@DatabaseField(columnName="ContactImage")
	private String contact_image="";

	@SerializedName("ringtone_file")
	@DatabaseField(columnName="ContactRingtone")
	private String contact_ringtone="";


	@SerializedName("connection_status")
	@DatabaseField(columnName="ContactStatus")
	private String connection_status="NC";


	@SerializedName("connections_id")
	@DatabaseField(columnName="ConnectionsId")
	private String connection_id="";

	@SerializedName("my_display_name")
	@DatabaseField(columnName="my_display_name")
	private String my_name = "";
	
	@SerializedName("my_ringtone_file")
	@DatabaseField(columnName="my_ringtone_file")
	private String my_ringtone = "";
	
	@SerializedName("thumbnailpic_senderEnd_url_thumb")
	@DatabaseField(columnName="my_image_path_thumb")
	private String my_image_thumb = "";
	
	
	@SerializedName("thumbnailpic_senderEnd_url_normal")
	@DatabaseField(columnName="my_image_path_normal")
	private String my_image_normal = "";
	
	
	public boolean isPurchase_status() {
		return purchase_status;
	}

	public void setPurchase_status(boolean purchase_status) {
		this.purchase_status = purchase_status;
	}

	@SerializedName("purchase_status")
	@DatabaseField(columnName="purchase_status")
	private boolean purchase_status= false;
	
	@SerializedName("send_status")
	private boolean send_status = false;
	
	
	public boolean isSend_status() {
		return send_status;
	}

	public void setSend_status(boolean send_status) {
		this.send_status = send_status;
	}

	public String getMy_image_thumb() {
		return my_image_thumb;
	}

	public void setMy_image_thumb(String my_image_thumb) {
		this.my_image_thumb = my_image_thumb;
	}
	
	

	public String getMy_image_normal() {
		return my_image_normal;
	}

	public void setMy_image_normal(String my_image_normal) {
		this.my_image_normal = my_image_normal;
	}

	public String getMy_ringtone() {
		return my_ringtone;
	}

	public void setMy_ringtone(String my_ringtone) {
		this.my_ringtone = my_ringtone;
	}

	public String getMy_name() {
		return my_name;
	}

	public void setMy_name(String my_name) {
		this.my_name = my_name;
	}

	public String getConnection_id() {
		return connection_id;
	}

	public void setConnection_id(String connection_id) {
		this.connection_id = connection_id;
	}


	public String getConnection_status() {
		return connection_status;
	}

	public void setConnection_status(String connection_status) {
		this.connection_status = connection_status;
	}

	public String getContact_id() {
		return contact_id;
	}

	public void setContact_id(String contact_id) {
		this.contact_id = contact_id;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_image() {
		return contact_image;
	}

	public void setContact_image(String contact_image) {
		this.contact_image = contact_image;
	}

	public String getContact_ringtone() {
		return contact_ringtone;
	}

	public void setContact_ringtone(String contact_ringtone) {
		this.contact_ringtone = contact_ringtone;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	@Override 
	public boolean equals(Object obj) {
		return (obj instanceof Contacts) && this.contact_id == ((Contacts)obj).getContact_id();
	}

	@Override
	public String toString() {
		return "Contacts [contact_type=" + contact_type + ", raw_contact_id="
				+ raw_contact_id + ", contact_number=" + contact_number
				+ ", contact_id=" + contact_id + ", contact_name="
				+ contact_name + ", contact_image=" + contact_image
				+ ", contact_ringtone=" + contact_ringtone
				+ ", connection_status=" + connection_status
				+ ", connection_id=" + connection_id + ", my_name=" + my_name
				+ ", my_ringtone=" + my_ringtone + ", my_image=" + my_image_thumb
				+ ", purchase_status=" + purchase_status + ", send_status="
				+ send_status + "]";
	} 

	
	
	
}
