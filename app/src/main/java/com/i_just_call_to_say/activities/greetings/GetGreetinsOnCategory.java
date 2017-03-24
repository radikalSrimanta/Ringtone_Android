package com.i_just_call_to_say.activities.greetings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.imageLoder.ImageLoader;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.PurchaseUtility;
import com.i_just_call_to_say.activities.about.PurchaseUtility.IUpgrade;
import com.i_just_call_to_say.activities.about.UpdateInAppPurchaseApiManager;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.i_just_call_to_say.dto.CardCategory;
import com.i_just_call_to_say.dto.Contacts;
import com.i_just_call_to_say.dto.GreetingsCard;
import com.utility.ActivityController;
import com.utility.AlertDialogCallBack;
import com.utility.CustomDialogUtility;
import com.utility.view.CustomTextView;

public class GetGreetinsOnCategory extends RingToneBaseActivity {
	
	private GridView grid_image;
	private CardCategory cardCategory;
	private GetGreetingsListOnActegoryApiManager getGreetingsListOnActegoryApiManager;
	private List<GreetingsCard> greetingscardlist;
	public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoaYuvdYTQyFRWKnjGJz/8Td6tJ4maDmO8rI/VeDN5cMzR5dB6bQk4Ex8oWhAlb6QmOcN/Jg6ncfvBXKp9qY0OdNZnfE1Dp24O8aEDNJdskxvi6Idz1bXeUn04EfpFJQNFcuamuULCWWwZJrmiQMWgix5XDSOVMq8+p2YAiS+I9kSJd3kG71jOst7YYI1im2AY8w9evVRIZkLcIgqFsbaZyNPVbEu7+M5G5G/QUMajPW5e969lr5IoqO7tpjdp2lPmLohDm6C62g06kfTGNh7XNKmkQd115G5ICFc44QJ4mksFBc5c6bmb7jt8L1A6FBcTCrLMKtgcjP8gee3fURXdwIDAQAB";
	public static final String SKU = "android.test.purchased"; 
	private Purchase purchase=null;
	private Contacts contacts;
	private PurchaseUtility purchaseUtility;
	private Bundle mbundle;
	private CustomTextView tv_header;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendingcardgreetings);
		getIntentData();
		initView();
//		setTabSelected(RingToneBaseActivity.GET_GREETINGS,cardCategory.getCategory_name());
		onGridClickListner();
		attachListener();
		call_greetingCardService();
	}
	
	private void call_greetingCardService() {
		Object[] values={user.getAccess_token(),cardCategory.getCategory_id(),RingToneBaseApplication.device_token};
		getGreetingsListOnActegoryApiManager=new GetGreetingsListOnActegoryApiManager(this, ringToneBaseApplication, "Loading cards...        ", values);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		Object[] values={user.getAccess_token(),cardCategory.getCategory_id(),RingToneBaseApplication.device_token};
//		getGreetingsListOnActegoryApiManager=new GetGreetingsListOnActegoryApiManager(this, ringToneBaseApplication, "Loading cards...        ", values);
	}
	
	private void initView(){
//		super.initBaseView();
		imageLoader = new ImageLoader(GetGreetinsOnCategory.this);
		grid_image=(GridView)findViewById(R.id.grid_image);
		btn_arrow = (Button) findViewById(R.id.btn_arrow);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_header = (CustomTextView) findViewById(R.id.tv_header);
		tv_header.setText(cardCategory.getCategory_name());
	}
	
	private void getIntentData(){
		try {
			Bundle bundle=getIntent().getExtras();
			cardCategory=(CardCategory) bundle.getSerializable("cardCategory");
			contacts = (Contacts) bundle.getSerializable("contacts");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getAllGreetingsList(List<GreetingsCard> greetingscard_list){
		this.greetingscardlist=greetingscard_list;
		getGreetingsListOnActegoryApiManager=null;
		if(greetingscard_list!=null && greetingscard_list.size()>0){
		SendGreetingsGridAdapter sendGreetingsGridAdapter=new SendGreetingsGridAdapter(this, (ArrayList<GreetingsCard>)greetingscard_list,imageLoader);
		grid_image.setAdapter(sendGreetingsGridAdapter);
		}
	}
	
	
	
	private void onGridClickListner(){
		grid_image.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
			mbundle =new Bundle();
			mbundle.putSerializable("cardcategory", cardCategory);
			if (contacts!=null) {
				mbundle.putSerializable("contacts", contacts);	
			}
			mbundle.putSerializable("GreetingsCard",(Serializable) greetingscardlist.get(position));	
				
				
			GreetingsCard greetingsCard = greetingscardlist.get(position);
//			System.out.println("user.getPurchase_status()"+ user.getPurchase_status());
			if(greetingsCard.getIs_free().equalsIgnoreCase("Y") || user.getPurchase_status().equalsIgnoreCase("true") ){
//				mbundle =new Bundle();
//				mbundle.putSerializable("cardcategory", cardCategory);
//				if (contacts!=null) {
//					mbundle.putSerializable("contacts", contacts);	
//				}
//				mbundle.putSerializable("GreetingsCard",(Serializable) greetingscardlist.get(position));
				//System.out.println("large ");
				ActivityController.startNextActivity(GetGreetinsOnCategory.this, GetSendGreetingsLargeActivity.class, mbundle, false);
			}else {
				CustomDialogUtility.showCallbackMessageWithOkCancel("Upgrade to full version to view all the greeting cards. Do you want to upgrade it now ?", GetGreetinsOnCategory.this, new AlertDialogCallBack() {
					
					@Override
					public void onSubmitWithEditText(String text) {
						
					}
					
					@Override
					public void onSubmit() {
						try {
							purchaseUtility = PurchaseUtility.getPurchaseUtilityInstance(GetGreetinsOnCategory.this, PurchaseUtility.USER_SKU, new IUpgrade() {
//								@Override
//								public void onUpgradeFinished() {
//									update_callService();
//								}

								@Override
								public void onPurchaseDetails(Inventory inv) {
									if (inv.hasPurchase(PurchaseUtility.USER_SKU)) {
										purchaseUtility.startConsumePurchase(inv.getPurchase(PurchaseUtility.USER_SKU));
//										update_callService();
									}else{
										purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
									}
								}

								@Override
								public void onConsumeFinished(boolean isConsumed) {
//									if (isConsumed) {
//										purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
//									} else {
//										Toast.makeText(getApplicationContext(), "Purchasing error please try again.", Toast.LENGTH_LONG).show();
//									}
								}

								@Override
								public void onUpgradeFinished(boolean isSuccess, IabResult result,Purchase info) {
									if (isSuccess) {
										purchaseUtility.startConsumePurchase(info);
										update_callService();
									} else {
										Toast.makeText(getApplicationContext(), "Purchasing unsuccessful please try again.", Toast.LENGTH_LONG).show();
//										purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
									}
								}
							});
							purchaseUtility.initBillingHelper();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
				
			}
		});
	}

	private void update_callService(){
		Object[] values={user.getAccess_token(),"YES",RingToneBaseApplication.device_token};
 	    UpdateInAppPurchaseApiManager updateInAppPurchaseApiManager=new UpdateInAppPurchaseApiManager(GetGreetinsOnCategory.this, ringToneBaseApplication, user,"Updating Purchase...       ", values);
	}
	
	public void update_serviceCallBack() {
		initAdView();
		if(mbundle!=null)
		ActivityController.startNextActivity(GetGreetinsOnCategory.this, GetSendGreetingsLargeActivity.class, mbundle, false);
	}
	
	
	private void attachListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		purchaseUtility.onActivityResult(requestCode, resultCode, data);
	}

}
