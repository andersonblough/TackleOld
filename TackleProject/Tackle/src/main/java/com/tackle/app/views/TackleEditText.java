package com.tackle.app.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Bill on 12/3/13.
 */
public class TackleEditText extends EditText {
    public TackleEditText(Context context) {
        super(context);
    }

    public TackleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TackleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Activity addActivity = (Activity) getContext();
            addActivity.finish();
            addActivity.overridePendingTransition(0,0);
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
