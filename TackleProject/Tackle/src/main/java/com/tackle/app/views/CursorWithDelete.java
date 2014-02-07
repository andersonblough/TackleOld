package com.tackle.app.views;

import android.database.AbstractCursor;
import android.database.Cursor;

/**
 * Created by Bill on 1/28/14.
 */
public class CursorWithDelete extends AbstractCursor {
    private Cursor cursor;
    private int posToIgnore;

    public CursorWithDelete(Cursor cursor, int posToRemove){
        this.cursor = cursor;
        posToIgnore = posToRemove;
    }
    @Override
    public int getCount() {
        return cursor.getCount() - 1;
    }

    @Override
    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    @Override
    public String getString(int column) {
        return cursor.getString(column);
    }

    @Override
    public short getShort(int column) {
        return cursor.getShort(column);
    }

    @Override
    public int getInt(int column) {
        return cursor.getInt(column);
    }

    @Override
    public long getLong(int column) {
        return cursor.getLong(column);
    }

    @Override
    public float getFloat(int column) {
        return cursor.getFloat(column);
    }

    @Override
    public double getDouble(int column) {
        return cursor.getDouble(column);
    }

    @Override
    public boolean isNull(int column) {
        return cursor.isNull(column);
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        if (newPosition < posToIgnore){
            cursor.moveToPosition(newPosition);
        }
        else {
            cursor.moveToPosition(newPosition+1);
        }
        return true;
    }
}
