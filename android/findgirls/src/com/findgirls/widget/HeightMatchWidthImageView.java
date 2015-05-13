package com.findgirls.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HeightMatchWidthImageView extends ImageView {

    public HeightMatchWidthImageView(Context context) {
        super(context);
    }

    public HeightMatchWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightMatchWidthImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // ceil not round - avoid thin vertical gaps along the left/right edges
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (width*3+1)/4;
        setMeasuredDimension(width, height);
    }

}

