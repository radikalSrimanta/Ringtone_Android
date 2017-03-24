package com.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.i_just_call_to_say.R;
import com.utility.view.CustomButton;
import com.utility.view.CustomEditText;
import com.utility.view.CustomTextView;

public class CustomDialogUtility {

    public static void showCallbackMessageWithOk(String message, Activity mContext, final AlertDialogCallBack callback) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setCancelable(false);

        CustomTextView et_name = (CustomTextView) dialog.findViewById(R.id.et_message);
        CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
        CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
        CustomButton btn_ok = (CustomButton) dialog.findViewById(R.id.btn_ok);

        et_name.setText(message);
        btn_ok.setVisibility(View.VISIBLE);

        btn_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSubmit();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCancel();
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onSubmit();
                dialog.dismiss();
            }
        });

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showCallbackMessageWithOkCancel(String message, Activity mContext, final AlertDialogCallBack callback) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setCancelable(false);

        CustomTextView et_name = (CustomTextView) dialog.findViewById(R.id.et_message);
        CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
        CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
        CustomButton btn_ok = (CustomButton) dialog.findViewById(R.id.btn_ok);

        et_name.setText(message);
        btn_accept.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);

        btn_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSubmit();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCancel();
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onSubmit();
                dialog.dismiss();
            }
        });

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showMessageWithOk(String message, Activity mContext) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_layout);
            dialog.setCancelable(false);

            CustomTextView et_name = (CustomTextView) dialog.findViewById(R.id.et_message);
            CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
            CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
            CustomButton btn_ok = (CustomButton) dialog.findViewById(R.id.btn_ok);

            et_name.setText(message);
            btn_ok.setVisibility(View.VISIBLE);

            btn_accept.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_ok.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showMessageWithOk(String message, Context mContext) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_layout);
            dialog.setCancelable(false);

            CustomTextView et_name = (CustomTextView) dialog.findViewById(R.id.et_message);
            CustomButton btn_accept = (CustomButton) dialog.findViewById(R.id.btn_accept);
            CustomButton btn_cancel = (CustomButton) dialog.findViewById(R.id.btn_cancel);
            CustomButton btn_ok = (CustomButton) dialog.findViewById(R.id.btn_ok);

            et_name.setText(message);
            btn_ok.setVisibility(View.VISIBLE);

            btn_accept.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_ok.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
