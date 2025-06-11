package com.example.quanlycongviecapp;

import android.content.Context;
import android.util.AttributeSet;

public class SquareButton extends androidx.appcompat.widget.AppCompatButton {
    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}