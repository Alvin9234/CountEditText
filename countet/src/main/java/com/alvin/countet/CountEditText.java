package com.alvin.countet;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 限制 EditText 的输入范围
 *
 * maxLength：EditText的输入上限
 *
 * textSize：EditText和显示字数的TextView的字体大小
 * 在布局中声明使用时，textSize的单位要使用      px
 * 因为 TypedArray.getDimension()方法是获取某个dimen的值,如果是dp或sp的单位,将其乘以density，字体会比常用控件中的设置大许多，所以用的时候除以density就行了
 * 如果是px,则不乘，但是会随着系统字体大小改变
 *
 * hasClear:是否具有清除文本功能：true,具有；false,不具有
 *
 * hint: 设置EditText的提示
 *
 * bg_color: 设置EditText的背景
 * text_color: 设置EditText的文字颜色
 * hint_color: 设置EditText的提示文字颜色
 * count_color: 设置计数的提示文字颜色
 * 调用示例：
 * 在引用布局中自定义命名空间
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 *
 * <包名.CountEditText
    	android:id="@+id/edit_count"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
 		app:maxLength="100"
 		app:textSize="16dip"
 		app:hasClear="true"
 		app:hint="@string/feedback_hint"
    />
 */
public class CountEditText extends FrameLayout implements TextWatcher {
	private EditText mEditText;
	private TextView mTextView;
	private ImageView mClearContent;
    private int maxLength=0;
    private FrameLayout frameLayout;
	private float dimension;
	private boolean hasClear;//是否需要具有清除文本功能
	private String hint="";
	private String content="";//指定EditText内容
	private int bg_color;//EditText的背景
	private int text_color;//EditText的文字颜色
	private int hint_color;//EditText的hint颜色
	private int count_color;//计数的文本颜色
	public CountEditText(Context context) {
		super(context);
	}

	public CountEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float density = dm.density;

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.count_edittext);
		maxLength=mTypedArray.getInt(R.styleable.count_edittext_maxLength, 1);
		dimension = mTypedArray.getDimension(R.styleable.count_edittext_textSize, 12)/density;//没有声明，默认12号字体
		hasClear = mTypedArray.getBoolean(R.styleable.count_edittext_hasClear, false);
		hint = mTypedArray.getString(R.styleable.count_edittext_hint);
		content = mTypedArray.getString(R.styleable.count_edittext_text);
		bg_color = mTypedArray.getColor(R.styleable.count_edittext_bg_color,0xFFFFFFFF);//EditText背景默认白色
		hint_color = mTypedArray.getColor(R.styleable.count_edittext_hint_color,0xFFBFBFBF);//EditText提示颜色
		text_color = mTypedArray.getColor(R.styleable.count_edittext_text_color,0xFF000000);//EditText字体颜色默认黑色
		count_color = mTypedArray.getColor(R.styleable.count_edittext_count_color,0xFF888888);//计数字体默认灰色

		frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_edittext, this, true);
		mEditText = (EditText) frameLayout.findViewById(R.id.contentET);
		mTextView = (TextView) frameLayout.findViewById(R.id.contentTip);
		mClearContent = (ImageView) frameLayout.findViewById(R.id.mClearContent);

		mTextView.setText("0/"+maxLength);
		mTextView.setTextColor(count_color);
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dimension);
		mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dimension);
		mEditText.setHintTextColor(hint_color);
		mEditText.setBackgroundColor(bg_color);
		mEditText.setTextColor(text_color);
		//限制 EditText的输入范围
		mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		mEditText.addTextChangedListener(this);
		mTypedArray.recycle();

		if(!TextUtils.isEmpty(hint)){
			mEditText.setHint(hint);
		}
		if(!TextUtils.isEmpty(content)){
			mEditText.setText(content);
			mTextView.setText(content.length()+"/"+maxLength);
		}

		mClearContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.setText("");
			}
		});
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		mTextView.setText(s.length()+"/"+maxLength);
		if(hasClear){
			if(s.length()>0){
				mClearContent.setVisibility(View.VISIBLE);
				content = s.toString();
			}else{
				content = "";
			}
		}else{
			mClearContent.setVisibility(View.GONE);
			if(s.length()>0){
				content = s.toString();
			}else{
				content = "";
			}
		}
	}

	public EditText getmEditText() {
		return mEditText;
	}

	public void setMaxLength(int maxLength){
		if(maxLength>0) {
			this.maxLength=maxLength;
			mTextView.setText("0/" + maxLength);
			mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		}
	}

	public int getMaxLength() {
		return maxLength;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		if(!TextUtils.isEmpty(content)){
			mEditText.setText(content);
			mTextView.setText(content.length()+"/"+maxLength);
		}
	}

	public void setHint(String hint) {
		this.hint = hint;
		if(!TextUtils.isEmpty(hint)){
			mEditText.setHint(hint);
		}
	}

	public void setBg_color(int bg_color) {
		this.bg_color = bg_color;
		mEditText.setBackgroundColor(bg_color);
	}

	public void setCount_color(int count_color) {
		this.count_color = count_color;
		mTextView.setTextColor(count_color);
	}

	public void setHint_color(int hint_color) {
		this.hint_color = hint_color;
		mEditText.setHintTextColor(hint_color);
	}

	public void setText_color(int text_color) {
		this.text_color = text_color;
		mEditText.setTextColor(text_color);
	}
}
