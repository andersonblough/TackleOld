package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tackle.app.R;

/**
 * Created by Bill on 11/19/13.
 */
public class QuoteView extends RelativeLayout {
    public TextView quote, author;


    public QuoteView(Context context) {
        super(context);
    }

    public QuoteView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.quote_layout, this, true);

        quote = (TextView) findViewById(R.id.quote_text);
        author = (TextView) findViewById(R.id.author_text);

    }

    public QuoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
