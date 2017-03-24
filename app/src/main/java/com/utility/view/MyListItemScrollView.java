package com.utility.view;

import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

public class MyListItemScrollView extends ScrollingMovementMethod {

    @Override
    public void onTakeFocus(TextView widget, Spannable text, int dir) {
        super.onTakeFocus(widget, text, dir);
    }

    @Override
    public boolean onKeyDown(TextView widget, Spannable buffer,
            int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_DOWN:
            for (int i = 0, scrollAmount = getScrollAmount(widget); i < scrollAmount; i++) {
                down(widget, buffer);
            }
            return true;
        case KeyEvent.KEYCODE_DPAD_UP:
            for (int i = 0, scrollAmount = getScrollAmount(widget); i < scrollAmount; i++) {
                up(widget, buffer);
            }
            return true;
        default:
            return super.onKeyDown(widget, buffer, keyCode, event);
        }
    }

    private int getScrollAmount(TextView widget) {
        final int visibleLineCount = (int) ((1f * widget.getHeight()) / widget
                .getLineHeight());
        int scrollAmount = visibleLineCount - 1;
        if (scrollAmount < 1) {
            scrollAmount = 1;
        }
        return scrollAmount;
    }


}