package com.utility.view;


import com.i_just_call_to_say.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class CustomExpandableTextView extends TextView {

	
	public interface IExpand{
		public void onExpandComplete(View v);
		public void onCollapseCompleted(View v);
	}
	
	private static final int DEFAULT_TRIM_LENGTH = 100;
	private static final String ELLIPSIS = "...";

	private CharSequence originalText;
	private CharSequence trimmedText;
	private BufferType bufferType;
	private boolean trim = true;
	private int trimLength;
	private Context mContext;
	private IExpand iExpand;

	public CustomExpandableTextView(Context context) {
		super(context);
		mContext = context;
		setClickListener();
	}

	public CustomExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initFromXML(context, attrs);
		setClickListener();
	}
	
	
	public void expandText(IExpand iExpand){
		this.iExpand = iExpand;
		trim = !trim;
		setText();
		requestFocusFromTouch();
	}
	
	public void expandText(){
		trim = !trim;
		setText();
		requestFocusFromTouch();
	}

	public CustomExpandableTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initFromXML(context, attrs);
		setClickListener();
	}
	
	private void setClickListener(){
		//		setOnClickListener(new OnClickListener() {
		//		@Override
		//		public void onClick(View v) {
		//			trim = !trim;
		//			setText();
		//			requestFocusFromTouch();
		//		}
		//	});
	}

	private void setText() {
		super.setText(getDisplayableText(), bufferType);
		System.out.println("MoreBtn : " + getText().length() + " " + trimmedText.length() +" IExpand: " + iExpand);
		if(iExpand!= null && getText().length() > trimmedText.length() ){
			iExpand.onExpandComplete(this);
		}else if(iExpand!= null && getText().length() <= trimmedText.length()){
			iExpand.onCollapseCompleted(this);
		}
	}

	private CharSequence getDisplayableText() {
		return trim ? trimmedText : originalText;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		originalText = text;
		trimmedText = getTrimmedText(text);
		bufferType = type;
		setText();
	}

	private CharSequence getTrimmedText(CharSequence text) {
		if (originalText != null && originalText.length() > trimLength) {
			return new SpannableStringBuilder(originalText, 0, trimLength + 1)
					.append(ELLIPSIS);
		} else {
			return originalText;
		}
	}

	public CharSequence getOriginalText() {
		return originalText;
	}

	public void setTrimLength(int trimLength) {
		this.trimLength = trimLength;
		trimmedText = getTrimmedText(originalText);
		setText();
	}

	public int getTrimLength() {
		return trimLength;
	}

	private void initFromXML(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			System.out.println("initilaizing!!!");
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CustomFont);
			System.out.println("length!!!" + a.length());
			if (a.length() > 0) {
				for (int i = 0; i < a.length(); i++) {
					int attr = a.getIndex(i);
					if (attr == R.styleable.CustomFont_fontFace) {
						int typefaceValue = a.getInteger(attr, -1);
						setFontFace(typefaceValue);
					}
					if (attr == R.styleable.CustomFont_trimLength) {
						this.trimLength = a.getInt(attr, DEFAULT_TRIM_LENGTH);
						System.out.println("!!!!!!!Trim text");
					}

				}
			}
			a.recycle();
		}

	}

	private void setFontFace(int font_type) {
		String fontType = GetFontFace.getFontFace(font_type);

		System.out.println("FF: " + fontType + "font_val " + font_type);
		if (!"".equals(fontType)) {
			Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/" + fontType);
			this.setTypeface(tf);
		}

	}
}
