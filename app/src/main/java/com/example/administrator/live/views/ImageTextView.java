package com.example.administrator.live.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.administrator.live.R;

public class ImageTextView extends LinearLayout {

    Drawable background;
    String text;
    int imageWidth;
    int imageHeight;
    int textSize;
    Color textColor;

    private final String mNameSpace = "http://schemas.android.com/apk/res/chen.chenximobilesafe";


    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrFromXml(context, attrs);
        initView(attrs);

    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrFromXml(context, attrs);

        initView(attrs);

    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttrFromXml(context, attrs);

        initView(attrs);
    }

    private void getAttrFromXml(Context context,AttributeSet attrs){
        //1
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);

        //2
        background = typedArray.getDrawable(R.styleable.ImageTextView_background);
        text = typedArray.getString(R.styleable.ImageTextView_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textColor = Color.valueOf(typedArray.getColor(R.styleable.ImageTextView_textColor, Color.WHITE));
        }
        textSize = typedArray.getInt(R.styleable.ImageTextView_textSize, 16);
        imageWidth = typedArray.getInt(R.styleable.ImageTextView_imageWidth, 80);
        imageHeight = typedArray.getInt(R.styleable.ImageTextView_imageHeight, 80);

        //4
        typedArray.recycle();
    }


    private void initView(AttributeSet attrs){
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(background);
        LayoutParams layoutParams = new LayoutParams(imageWidth, imageHeight);
        imageView.setLayoutParams(layoutParams);
        addView(imageView);

        if (null != text && !text.isEmpty()) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(getContext().getColor(R.color.white));
            }
            addView(textView);
        }

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }
}
