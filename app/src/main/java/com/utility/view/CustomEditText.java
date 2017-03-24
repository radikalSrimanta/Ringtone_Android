package com.utility.view;

import com.i_just_call_to_say.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

	private Context mContext;

	public CustomEditText(Context context) {
		super(context);
		mContext = context;
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initFromXML(context, attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initFromXML(context, attrs);
	}

	private void initFromXML(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);

			if (a.length() > 0) {
				for (int i = 0; i < a.length(); i++) {
					int attr = a.getIndex(i);
					if (attr == R.styleable.CustomFont_fontFace) {
						int typefaceValue = a.getInteger(attr, -1);
						setFontFace(typefaceValue);
					}

				}
			}
			a.recycle();
		}

	}

	private void setFontFace(int font_type) {
		String fontType = GetFontFace.getFontFace(font_type);

		//System.out.println("FF: " + fontType + "font_val " + font_type);
		if (!"".equals(font_type)) {
			Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontType);
			this.setTypeface(tf);
		}

	}
}
