package com.i_just_call_to_say.activities.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.activities.about.PurchaseUtility.IUpgrade;
import com.i_just_call_to_say.activities.base.RingToneBaseActivity;
import com.i_just_call_to_say.activities.base.RingToneBaseApplication;
import com.utility.view.CustomButton;

public class AboutRingToneActivity extends RingToneBaseActivity {
	
	public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoaYuvdYTQyFRWKnjGJz/8Td6tJ4maDmO8rI/VeDN5cMzR5dB6bQk4Ex8oWhAlb6QmOcN/Jg6ncfvBXKp9qY0OdNZnfE1Dp24O8aEDNJdskxvi6Idz1bXeUn04EfpFJQNFcuamuULCWWwZJrmiQMWgix5XDSOVMq8+p2YAiS+I9kSJd3kG71jOst7YYI1im2AY8w9evVRIZkLcIgqFsbaZyNPVbEu7+M5G5G/QUMajPW5e969lr5IoqO7tpjdp2lPmLohDm6C62g06kfTGNh7XNKmkQd115G5ICFc44QJ4mksFBc5c6bmb7jt8L1A6FBcTCrLMKtgcjP8gee3fURXdwIDAQAB";
	public static final String SKU = "android.test.purchased"; 
	public CustomButton btn_upgrade;
	public RelativeLayout rl_footer;	
	private PurchaseUtility purchaseUtility;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		initView();
		setTabSelected(RingToneBaseActivity.ABOUT,"ABOUT");
	}
	
	private void initView(){
		super.initBaseView();
		btn_upgrade = (CustomButton) findViewById(R.id.btn_upgrade);
		rl_footer = (RelativeLayout) findViewById(R.id.include_02);
//		System.out.println("user.getPurchase_status()."+ user.getPurchase_status());
		
		if (user.getPurchase_status().equalsIgnoreCase("true")) {
			btn_upgrade.setVisibility(View.GONE);
		}else{
			btn_upgrade.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onUpgradeClick(View v) {
		if (user.getPurchase_status().equalsIgnoreCase("true")) {
			btn_upgrade.setVisibility(View.GONE);
		}else{
			btn_upgrade.setVisibility(View.VISIBLE);
			purchaseUtility = PurchaseUtility.getPurchaseUtilityInstance(AboutRingToneActivity.this,PurchaseUtility.USER_SKU ,new IUpgrade() {
			/*@Override
			public void onUpgradeFinished() {
				AboutRingToneActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						update_callService();
					}
				});
			}*/

			@Override
			public void onPurchaseDetails(Inventory inv) {
				if (inv.hasPurchase(PurchaseUtility.USER_SKU)) {
					purchaseUtility.startConsumePurchase(inv.getPurchase(PurchaseUtility.USER_SKU));
				}else{
					purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
				}
			}

			@Override
			public void onConsumeFinished(boolean isConsumed) {
//				if (isConsumed) {
//					purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
//				} else {
//					Toast.makeText(getApplicationContext(), "Purchasing error please try again.", Toast.LENGTH_LONG).show();
//				}
			}

			@Override
			public void onUpgradeFinished(boolean isSuccess, IabResult result,	Purchase info) {
				if (isSuccess) {
					purchaseUtility.startConsumePurchase(info);
					update_callService();
				} else {
					Toast.makeText(getApplicationContext(), "Purchasing unsuccessful please try again.", Toast.LENGTH_LONG).show();
//					purchaseUtility.purchaseItem(PurchaseUtility.USER_SKU);
				}
			}
		});
		purchaseUtility.initBillingHelper();
		}
	}
	
	private void update_callService(){
		Object[] values={user.getAccess_token(),"YES",RingToneBaseApplication.device_token};
 	    UpdateInAppPurchaseApiManager updateInAppPurchaseApiManager=new UpdateInAppPurchaseApiManager(AboutRingToneActivity.this, ringToneBaseApplication, user,"Updating Purchase...       ", values);
	}
	
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        purchaseUtility.onActivityResult(requestCode, resultCode, data);
	    }
	 
}
