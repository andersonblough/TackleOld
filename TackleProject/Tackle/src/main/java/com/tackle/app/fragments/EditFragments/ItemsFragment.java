package com.tackle.app.fragments.EditFragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.Dialogs.ClearItemsDialog;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;
import com.tackle.app.data.TackleEvent;
import com.tackle.app.views.EnhancedListView;

/**
 * Created by Bill on 1/16/14.
 */
public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText itemText;
    ImageView amount;
    ImageView add;

    private int qtyOfItems;
    private String nameOfItem;
    private long mID;

    private Cursor mCursor;

    EnhancedListView listView;
    ItemsAdapter itemsAdapter;

    public ItemsFragment(Cursor cursor) {
        super();
        mCursor = cursor;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qtyOfItems = 1;

        itemsAdapter = new ItemsAdapter(getActivity(), null, false);

        if (mCursor.moveToFirst()) {
            mID = mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent._ID));
        }
        Bundle args = new Bundle();
        args.putLong("id", mID);
        getActivity().getSupportLoaderManager().initLoader(3, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_items, container, false);

        View headerView = inflater.inflate(R.layout.item_list_header, null, false);

        itemText = (EditText) headerView.findViewById(R.id.item_text);
        listView = (EnhancedListView) view.findViewById(R.id.item_list);
        listView.setAdapter(itemsAdapter);

        View footer = new View(getActivity());
        footer.setMinimumHeight(16);
        footer.setClickable(false);

        listView.addFooterView(footer);
        listView.addHeaderView(headerView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set edit text box to the title
                Cursor cursor = itemsAdapter.getCursor();
                cursor.moveToPosition(position - 1);
                itemText.setText(cursor.getString(cursor.getColumnIndex(TackleContract.ListItems.NAME)));
                //set qty of items
                qtyOfItems = cursor.getInt(cursor.getColumnIndex(TackleContract.ListItems.QUANTITY));
                //remove item from list
                Uri uri = ContentUris.withAppendedId(TackleContract.ListItems.CONTENT_URI, id);
                getActivity().getContentResolver().delete(uri, null, null);
                //show keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(itemText, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int count = listView.getCheckedItemCount();
                if (count == 1) {
                    mode.setTitle("1 Item");
                } else {
                    mode.setTitle(String.valueOf(count) + " Items");
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        long[] ids = listView.getCheckedItemIds();
                        ContentResolver cr = getActivity().getContentResolver();
                        for (int i = 0; i < ids.length; i++) {
                            long id = ids[i];
                            Uri uri = ContentUris.withAppendedId(TackleContract.ListItems.CONTENT_URI, id);
                            cr.delete(uri, null, null);
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


        amount = (ImageView) headerView.findViewById(R.id.amount);
        add = (ImageView) headerView.findViewById(R.id.add);
        //slideUp(add);
        //slideUp(amount);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues listItem = new ContentValues();
                listItem.put(TackleContract.ListItems.EVENT_ID, mID);
                listItem.put(TackleContract.ListItems.NAME, itemText.getText().toString());
                listItem.put(TackleContract.ListItems.QUANTITY, qtyOfItems);
                listItem.put(TackleContract.ListItems.STATUS, TackleEvent.Status.ONGOING);

                getActivity().getContentResolver().insert(TackleContract.ListItems.CONTENT_URI, listItem);

                itemText.setText("");
                qtyOfItems = 1;

            }
        });

        itemText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (itemText.getText().toString().isEmpty()) {
                    slideUp(amount);
                    slideUp(add);
                    //amount.setVisibility(View.GONE);
                    //add.setVisibility(View.GONE);
                } else if (!add.isShown()) {
                    slideDown(amount);
                    slideDown(add);
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (itemsAdapter.getTackledItemCount() == 0) {
            menu.removeItem(R.id.clear);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                long[] ids = itemsAdapter.getTackledItemsIds();
                ClearItemsDialog clearItemsDialog = new ClearItemsDialog(ids);
                clearItemsDialog.show(getActivity().getSupportFragmentManager(), ClearItemsDialog.class.getName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, -view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void slideUp(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {TackleContract.ListItems.ID, TackleContract.ListItems.NAME, TackleContract.ListItems.QUANTITY, TackleContract.ListItems.EVENT_ID, TackleContract.ListItems.STATUS};
        String selection = TackleContract.ListItems.EVENT_ID + "=" + String.valueOf(bundle.getLong("id"));
        return new CursorLoader(getActivity(), TackleContract.ListItems.CONTENT_URI, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        itemsAdapter.swapCursor(cursor);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        itemsAdapter.swapCursor(null);
    }

    private class ItemsAdapter extends CursorAdapter {

        public ItemsAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public int getTackledItemCount() {
            int count = 0;
            Cursor c = getCursor();
            if (c == null) {
                return 0;
            }
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(c.getColumnIndex(TackleContract.ListItems.STATUS)) == TackleEvent.Status.TACKLED) {
                        count++;
                    }
                } while (mCursor.moveToNext());
            }

            return count;
        }

        public long[] getTackledItemsIds() {
            long[] ids = new long[getTackledItemCount()];
            int position = 0;
            Cursor c = getCursor();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                if (c.getInt(c.getColumnIndex(TackleContract.ListItems.STATUS)) == TackleEvent.Status.TACKLED) {
                    ids[position] = c.getLong(c.getColumnIndex(TackleContract.ListItems.ID));
                    position++;
                }
            }
            return ids;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cell_item, viewGroup, false);
            Holder holder = new Holder();
            holder.check = (ImageView) view.findViewById(R.id.check);
            holder.title = (TextView) view.findViewById(R.id.text);
            holder.quantity = (TextView) view.findViewById(R.id.qty);

            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int position = cursor.getPosition();

            Holder holder = (Holder) view.getTag();

            int tackled = cursor.getInt(cursor.getColumnIndex(TackleContract.ListItems.STATUS));
            int qty = cursor.getInt(cursor.getColumnIndex(TackleContract.ListItems.QUANTITY));
            String name = cursor.getString(cursor.getColumnIndex(TackleContract.ListItems.NAME));

            final int opposite;
            if (tackled == 1) {
                opposite = 0;
            } else {
                opposite = 1;
            }

            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentResolver cr = getActivity().getContentResolver();
                    long id = listView.getItemIdAtPosition(position + 1);
                    ContentValues update = new ContentValues();
                    update.put(TackleContract.ListItems.STATUS, opposite);
                    Uri uri = ContentUris.withAppendedId(TackleContract.ListItems.CONTENT_URI, id);
                    cr.update(uri, update, null, null);
                }
            });

            if (tackled == TackleEvent.Status.TACKLED) {
                holder.check.setImageResource(R.drawable.ic_item_checked);
            } else {
                holder.check.setImageResource(R.drawable.ic_item);
            }
            if (qty > 1) {
                holder.quantity.setText(String.valueOf(qty));
            } else {
                holder.quantity.setText("");
            }

            holder.title.setText(name);

        }
    }

    private class Holder {
        ImageView check;
        TextView title;
        TextView quantity;
    }
}
