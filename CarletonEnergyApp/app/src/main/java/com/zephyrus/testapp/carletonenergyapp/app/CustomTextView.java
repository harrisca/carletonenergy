package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

//public class CustomTextView extends TextView {
//    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(attrs);
//    }
//
//    public CustomTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//
//    }
//
//    public CustomTextView(Context context) {
//        super(context);
//        init(null);
//    }
//
//    private void init(AttributeSet attrs) {
//        if (attrs!=null) {
//            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
//            String fontName = "CustomFont";
//            if (fontName!=null) {
//                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
//                setTypeface(myTypeface, 1);
//            }
//            a.recycle();
//        }
//    }
//
//    @Override
//    public void setTypeface(Typeface tf, int style) {
//        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Rg.ttf");
//        Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Bd.ttf");
//        Typeface italicTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Lt.ttf");
//
//        if (style == Typeface.BOLD) {
//            super.setTypeface(boldTypeface/*, -1*/);
//        } else if(style == Typeface.ITALIC) {
//            super.setTypeface(italicTypeFace/*, -1*/);
//        } else{
//            super.setTypeface(normalTypeface/*, -1*/);
//        }
//    }
//
//
//}
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Aller_Rg.ttf");
        setTypeface(tf ,1);

    }

}
