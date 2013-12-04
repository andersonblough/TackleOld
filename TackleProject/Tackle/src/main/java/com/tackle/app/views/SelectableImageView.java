package com.tackle.app.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Bill on 12/3/13.
 */
public class SelectableImageView extends ImageView {
    public SelectableImageView(Context context) {
        super(context);
    }

    public SelectableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isEnabled()){
            setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_ATOP);
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            setColorFilter(null);
        }
        return super.onTouchEvent(event);
    }


}
