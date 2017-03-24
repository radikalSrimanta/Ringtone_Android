package com.i_just_call_to_say.activities.greetings;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;

import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.ReceivedGreetingsCard;
import com.utility.ActivityController;
import com.utility.view.CustomTextView;

public class GreetingsActivity extends RingToneBaseActivity  {

	private ListView greetings_list;
	private RadioButton rbtn_received, rbtn_send;
	private List<CardCategory> cardcategorylist;
	private List<ReceivedGreetingsCard> receivedGreetingsCards;
	private SendCategoryAdapter sendCategoryAdapter;
	private ReceivingGreetingsAdapter receivingGreetingsAdapter;
	private int flag = 0;
	private Contacts contacts;
	public CustomTextView tv_msg;
	private final int VIEW_GREETING_REQUEST_CODE = 1001;
	private String from;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greetings);
		readFromBundle();
		initView();
		onClickAdapter();
		setTabSelected(RingToneBaseActivity.GREETINGS,"GREETINGS");
	}
	
	private void readFromBundle() {
		try {
			contacts=(Contacts) getIntent().getExtras().getSerializable("contact");
			from= (String) getIntent().getExtras().getString("from");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void onClickAdapter() {
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		greetings_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,	int position, long arg3) {
				if (flag == 1) {
					CardCategory cardCategory = cardcategorylist.get(position);
					Bundle bundle = new Bundle();
					bundle.putSerializable("cardCategory", cardCategory);
					if(contacts!=null)
					bundle.putSerializable("contacts", contacts);
					ActivityController.startNextActivity(GreetingsActivity.this, GetGreetinsOnCategory.class, bundle, false);
				}
				else{
					ReceivedGreetingsCard greetingsCard = (ReceivedGreetingsCard) greetings_list.getAdapter().getItem(position);
					Bundle bundle = new Bundle();
					bundle.putSerializable("receive_greeting", greetingsCard);
					ActivityController.startNextActivityForResult(GreetingsActivity.this, ViewReceiveGreetingActivity.class, bundle, false, VIEW_GREETING_REQUEST_CODE);
					finish();
				}
			}
		});
		
	}
	
	

	private void initView(){
		super.initBaseView();
		imageLoader = new ImageLoader(GreetingsActivity.this);
		tv_msg = (CustomTextView) findViewById(R.id.tv_msg);
		greetings_list=(ListView)findViewById(R.id.lv_greetings);
		rbtn_received=(RadioButton)findViewById(R.id.rbtn_received);
		rbtn_send=(RadioButton)findViewById(R.id.rbtn_send);
		
		if(from !=null && from.equals("viewgreeting")){
			rbtn_received.setChecked(true);
			onReceivedGreetingsClick(rbtn_received);
		}else{
			rbtn_send.setChecked(true);
		    onSendGreetingsClick(rbtn_send);
		}
		
		if (contacts==null) {
			btn_menu.setVisibility(View.VISIBLE);
			btn_back.setVisibility(View.GONE);
		}else{
			btn_menu.setVisibility(View.GONE);
			btn_back.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(rbtn_received.isChecked())
		callReceivedGreetings();
		
	}
	
	private void callReceivedGreetings() {
		Object[] values = { user.getAccess_token(),RingToneBaseApplication.device_token};
		ReceiveGreetingsApiManager receiveGreetingsApiManager = new ReceiveGreetingsApiManager(this, ringToneBaseApplication, "Loading...           ", values);
	}
	
	public void getCateryList(List<CardCategory> cardcategorylist) {
		if (receivedGreetingsCards != null)
			receivedGreetingsCards.clear();
		if (this.cardcategorylist != null)
			this.cardcategorylist.clear();
		if (sendCategoryAdapter != null)
			sendCategoryAdapter.clear();
		if (receivingGreetingsAdapter != null)
			receivingGreetingsAdapter.clear();
		this.cardcategorylist = cardcategorylist;
		sendCategoryAdapter = new SendCategoryAdapter(this,	this.cardcategorylist, user);
		greetings_list.setAdapter(sendCategoryAdapter);
	}

	
	public void onReceivedGreetingsClick(View v) {
		flag = 0;
		if (receivedGreetingsCards != null)
			receivedGreetingsCards.clear();
		if (cardcategorylist != null)
			cardcategorylist.clear();
		if (sendCategoryAdapter != null)
			sendCategoryAdapter.clear();
		if (receivingGreetingsAdapter != null)
			receivingGreetingsAdapter.clear();
		callReceivedGreetings();
	}

	public void onSendGreetingsClick(View v) {
		flag = 1;
		tv_msg.setVisibility(View.GONE);
		Object[] values = { user.getAccess_token(),RingToneBaseApplication.device_token };
		GetCardCategoryApiManager getCardCategoryApiManager = new GetCardCategoryApiManager(this, ringToneBaseApplication, "Loading...           ", values);
	}
	
	public void receivingCardDetails(List<ReceivedGreetingsCard> greetingscardlist) {
		
		if (receivedGreetingsCards != null)
			receivedGreetingsCards.clear();
		if (cardcategorylist != null)
			cardcategorylist.clear();
		if (sendCategoryAdapter != null)
			sendCategoryAdapter.clear();
		if (receivingGreetingsAdapter != null)
			receivingGreetingsAdapter.clear();
		
		this.receivedGreetingsCards = greetingscardlist;
		receivingGreetingsAdapter = new ReceivingGreetingsAdapter(this,	receivedGreetingsCards, user, imageLoader);
		greetings_list.setAdapter(receivingGreetingsAdapter);
		System.out.println("receiced");
		tv_msg.setVisibility(View.GONE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	

}
