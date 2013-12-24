package com.tackle.app.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tackle.app.R;

import java.lang.reflect.Array;

/**
 * Created by Bill on 12/17/13.
 */
public class ColorPicker extends LinearLayout {
    Context mContext;

    public EditText mCategoryTitle;
    public GridView mColorGrid;
    private ColorPickerAdapter mAdapter;
    private View previousView;
    public int mColorRes;


    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_picker_layout, this, true);

        mCategoryTitle = (EditText) findViewById(R.id.category_text);
        mColorGrid = (GridView) findViewById(R.id.color_grid);

        mAdapter = new ColorPickerAdapter();

        mColorGrid.setAdapter(mAdapter);
        mColorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectedColor(view);
                mColorRes = mAdapter.colors[position];
            }
        });
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private class ColorPickerAdapter extends BaseAdapter{

        int[] colors = {R.color.umber, R.color.firetruck, R.color.illini,
                R.color.bigbird, R.color.leprechaun, R.color.aquateen,
                R.color.tidal, R.color.blueblue, R.color.nurple,
                R.color.plum, R.color.pinkberry, R.color.bark };


        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.color_swatch, parent, false);
            }

            ImageView check = (ImageView) view.findViewById(R.id.check);
            ImageView color = (ImageView) view.findViewById(R.id.color);
            color.setColorFilter(mContext.getResources().getColor(colors[position]), PorterDuff.Mode.SRC_ATOP);

            return view;
        }

        public void setSelectedColor(View view){
            if (previousView != null){
                ImageView check = (ImageView) previousView.findViewById(R.id.check);
                check.setVisibility(View.GONE);
            }

            previousView = view;
            ImageView checked = (ImageView) view.findViewById(R.id.check);
            checked.setVisibility(View.VISIBLE);
        }
    }
}
