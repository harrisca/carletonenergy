package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Aller_bd extends TextView {
        public Aller_bd(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(attrs);
        }

        public Aller_bd(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(attrs);

        }

        public Aller_bd(Context context) {
            super(context);
            init(null);
        }

        private void init(AttributeSet attrs) {
            if (attrs!=null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Aller_bd);
                String fontName = a.getString(R.styleable.Aller_bd_fontName);
                if (fontName!=null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                    setTypeface(myTypeface);
                }
                a.recycle();
            }
        }


}
