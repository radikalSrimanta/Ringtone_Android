package com.i_just_call_to_say.activities.about;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabHelper.OnConsumeFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.IabHelper.QueryInventoryFinishedListener;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.utility.DialogUtility;

public class PurchaseUtility implements OnIabSetupFinishedListener,
		OnIabPurchaseFinishedListener {

	public interface IUpgrade {
		public void onUpgradeFinished(boolean isSuccess,IabResult result, Purchase info);
		public void onPurchaseDetails(Inventory inv);
		public void onConsumeFinished(boolean isConsumed);
	}
	
	public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnnLPNJ39vXZ8bVs7Nt1mV0hsVjjOUiMo5KnFM+rd+NL69us9J/9skgfABTo9x03Trl2txv7UewBLQUyQCRLMsgrTxPhWz8Kk4MfxQFEqsP49qNkRK/U3LQU8xfpN2wWAPd0PTrm2CzdvkosF14NBbt3+3bKfM4YvtZKl6Y8sJlNOQTwiENknCpSi5nyNC+Cj8RnxRiYgQa1ZqJj9a8FBAWhS5+m/tbkdZCnH7vaqhxZpkCrDk/QXZpxdhfljKQTl7UO1OohB4JpNrv3/AWhtPXIiXnlGp89H+PojqAGlH6SyybbXDh1h1lLBns5gDlvwXn2MUxUL5Bsncem/VPzxuwIDAQAB";
	private  String SKU="";// = "android.test.purchased"; // Replace this with your item ID;
	
	public static final String USER_SKU = "i_just_called_to_say.";
	//public static final String FRIEND_SKU = "android.test.purchased"; 
	
	private static IabHelper billingHelper;
	
	private static PurchaseUtility purchaseUtility;
	private static Activity activity;
	private static IUpgrade iUpgrade;
	
	
	public PurchaseUtility(Activity activity,String SKU,IUpgrade iUpgrade){ 
		PurchaseUtility.activity = activity;
		PurchaseUtility.iUpgrade = iUpgrade;
		this.SKU = SKU;
	}
	
	public static synchronized PurchaseUtility getPurchaseUtilityInstance(Activity activity,String SKU, IUpgrade iUpgrade){
		///if(purchaseUtility == null)
		purchaseUtility =  new PurchaseUtility(activity,SKU,iUpgrade);
		return purchaseUtility;
	}
	
	
	public void removePurchaseService(){
		purchaseUtility = null;
		if(billingHelper != null)
			billingHelper.dispose();
		billingHelper = null;
	}
	
	
	public void initBillingHelper() {
		billingHelper = new IabHelper(activity, BASE_64_KEY);
		billingHelper.startSetup(this);
	}
	
	/*public void startPurchase(){
		billingHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {
			
			@Override
			public void onQueryInventoryFinished(IabResult result, Inventory inv) {
				System.out.println("onQueryInventoryFinished: " + inv.hasPurchase(SKU));
				if(inv.hasPurchase(SKU)){
					Purchase purchase = inv.getPurchase(SKU);
					iUpgrade.onUpgradeFinished();
				}else{
					purchaseItem(SKU);
				}
			}
		});
	}*/
	
	public void getPurchaseDetsils() {
		billingHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {

			@Override
			public void onQueryInventoryFinished(IabResult result, Inventory inv) {
				//System.out.println("onQueryInventoryFinished: "	+ inv.hasPurchase(SKU));
				if (iUpgrade != null)
					iUpgrade.onPurchaseDetails(inv);
			}
		});
	}
	
	public void startConsumePurchase(Purchase purchase){
		
		billingHelper.consumeAsync(purchase, new OnConsumeFinishedListener() {
			
			@Override
			public void onConsumeFinished(Purchase purchase, IabResult result) {
				iUpgrade.onConsumeFinished(result.isSuccess());
			}
		});
		
		/*billingHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {
			
			@Override
			public void onQueryInventoryFinished(IabResult result, Inventory inv) {
				//System.out.println("onQueryInventoryFinished: " + inv.hasPurchase(SKU));
				if(inv.hasPurchase(SKU)){
					Purchase purchase = inv.getPurchase(SKU);
					billingHelper.consumeAsync(purchase, new OnConsumeFinishedListener() {
						
						@Override
						public void onConsumeFinished(Purchase purchase, IabResult result) {
							iUpgrade.onConsumeFinished(result.isSuccess());
						}
					});
				}
			}
		});*/
	}
	
	
	
	//consumeAsync
	public void purchaseItem(String sku) {
		if(billingHelper != null){
			billingHelper.launchPurchaseFlow(activity, sku, 123, this);
		}
        
    }
	
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
    	billingHelper.flagEndAsync();
    	if (result.isFailure()) {
            dealWithPurchaseFailed(result);
        } else if (SKU.equals(info.getSku())) {
        	billingHelper.flagEndAsync();
            dealWithPurchaseSuccess(result, info);
        	Log.d("INAPP_BILLING","Error purchasing: 1 " + result);
           
        }
       
    }
	
	 private void dealWithPurchaseFailed(IabResult result) {
    	Log.d("INAPP_BILLING","Error purchasing: 2 " + result);
    	if(iUpgrade != null)
    		iUpgrade.onUpgradeFinished(false,result,null);
     }
	 
	 protected void dealWithPurchaseSuccess(IabResult result, Purchase info) {
	    	Log.d("INAPP_BILLING","Item purchased: " + result);
	        // DEBUG XXX
	        // We consume the item straight away so we can test multiple purchases
	    	if(iUpgrade != null)
	    		iUpgrade.onUpgradeFinished(true,result,info);
	       // END DEBUG
	    }

	@Override
	public void onIabSetupFinished(IabResult result) {
		if (result.isSuccess()) {
			Log.d("INAPP_BILLING", "In-app Billing set up" + result);
			//startPurchase();
			getPurchaseDetsils();
		} else {
			Log.d("INAPP_BILLING", "Problem setting up In-app Billing: "
					+ result);
			dealWithIabSetupFailure();
		}
	}

	private void dealWithIabSetupFailure() {
		DialogUtility.showMessageWithOk("Sorry this device doesn't supprot google inapp purchase", activity);
		if(iUpgrade != null)
    		iUpgrade.onUpgradeFinished(false,null,null);
		
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("INAPP_BILLING", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        // Pass on the activity result to the helper for handling
        if (!billingHelper.handleActivityResult(requestCode, resultCode, data)) {
        	  Log.d("INAPP_BILLING", "onActivityResult: " + billingHelper.handleActivityResult(requestCode, resultCode, data));
        } else {
            Log.d("INAPP_BILLING", "onActivityResult handled by IABUtil.");
        }
    }

}