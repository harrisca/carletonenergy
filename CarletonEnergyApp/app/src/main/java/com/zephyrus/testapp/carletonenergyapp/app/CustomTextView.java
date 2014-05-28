package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
    }

    private void init(AttributeSet attrs) {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_rg.ttf");
        setTypeface(tf ,1);

    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_bd.ttf"));
        } else if(style == Typeface.ITALIC) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_it.ttf"));
        } else{
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_rg.ttf"));
        }
    }


}
