package com.tackle.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Bill on 12/15/13.
 */
public class TackleProvider extends ContentProvider {

    private TackleDatabase mDB;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.Categories.TABLE_NAME, TackleContract.Categories.CATEGORIES);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.Categories.TABLE_NAME + "/#", TackleContract.Categories.CATEGORY_ID);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.TackleEvent.TABLE_NAME, TackleContract.TackleEvent.TACKLE_ITEMS);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.TackleEvent.TABLE_NAME + "/#", TackleContract.TackleEvent.TACKLE_ITEM_ID);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.Reminders.TABLE_NAME, TackleContract.Reminders.REMINDERS);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.Reminders.TABLE_NAME + "/#", TackleContract.Reminders.REMINDER_ID);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.ListItems.TABLE_NAME, TackleContract.ListItems.LIST_ITEMS);
        sUriMatcher.addURI(TackleContract.AUTHORITY, TackleContract.ListItems.TABLE_NAME + "/#", TackleContract.ListItems.LIST_ITEM_ID);
    }
    @Override
    public boolean onCreate() {
        mDB = new TackleDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case TackleContract.TackleEvent.TACKLE_ITEM_ID:
                queryBuilder.setTables(TackleContract.TackleEvent.TABLE_NAME);
                queryBuilder.appendWhere(TackleContract.TackleEvent._ID + "=" + uri.getLastPathSegment());
                break;
            case TackleContract.TackleEvent.TACKLE_ITEMS:
                queryBuilder.setTables(TackleContract.TackleEvent.TABLE_NAME);
                break;
            case TackleContract.Categories.CATEGORY_ID:
                queryBuilder.setTables(TackleContract.Categories.TABLE_NAME);
                queryBuilder.appendWhere(TackleContract.Categories._ID + "=" + uri.getLastPathSegment());
                break;
            case TackleContract.Categories.CATEGORIES:
                queryBuilder.setTables(TackleContract.Categories.TABLE_NAME);
                break;
            case TackleContract.ListItems.LIST_ITEM_ID:
                queryBuilder.setTables(TackleContract.ListItems.TABLE_NAME);
                queryBuilder.appendWhere(TackleContract.ListItems.ID + "=" + uri.getLastPathSegment());
                break;
            case TackleContract.ListItems.LIST_ITEMS:
                queryBuilder.setTables(TackleContract.ListItems.TABLE_NAME);
                break;
            case TackleContract.Reminders.REMINDER_ID:
                queryBuilder.setTables(TackleContract.Reminders.TABLE_NAME);
                queryBuilder.appendWhere(TackleContract.Reminders.ID + "=" + uri.getLastPathSegment());
                break;
            case TackleContract.Reminders.REMINDERS:
                queryBuilder.setTables(TackleContract.Reminders.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case TackleContract.Categories.CATEGORIES:
                return TackleContract.Categories.URI_TYPE_DIR;
            case TackleContract.Categories.CATEGORY_ID:
                return TackleContract.Categories.URI_TYPE_ITEM;
            case TackleContract.TackleEvent.TACKLE_ITEMS:
                return TackleContract.TackleEvent.URI_TYPE_DIR;
            case TackleContract.TackleEvent.TACKLE_ITEM_ID:
                return TackleContract.TackleEvent.URI_TYPE_ITEM;
            case TackleContract.Reminders.REMINDERS:
                return TackleContract.Reminders.URI_TYPE_DIR;
            case TackleContract.Reminders.REMINDER_ID:
                return TackleContract.Reminders.URI_TYPE_ITEM;
            case TackleContract.ListItems.LIST_ITEMS:
                return TackleContract.ListItems.URI_TYPE_DIR;
            case TackleContract.ListItems.LIST_ITEM_ID:
                return TackleContract.ListItems.URI_TYPE_ITEM;
            default:
                return null;

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues valuesIn) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDB.getWritableDatabase();
        Uri rowUri;
        ContentValues values = new ContentValues(valuesIn);
        long rowID;

        switch (uriType) {
            case TackleContract.TackleEvent.TACKLE_ITEMS:
                rowID = db.insert(TackleContract.TackleEvent.TABLE_NAME, null, values);
                if (rowID > 0){
                    rowUri = ContentUris.withAppendedId(TackleContract.TackleEvent.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return rowUri;
                }

                throw new SQLException("Failed to insert row into " + uri);
            case TackleContract.Categories.CATEGORIES:
                rowID = db.insert(TackleContract.Categories.TABLE_NAME, null, values);
                if (rowID > 0){
                    rowUri = ContentUris.withAppendedId(TackleContract.Categories.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return rowUri;
                }
            case TackleContract.ListItems.LIST_ITEMS:
                rowID = db.insert(TackleContract.ListItems.TABLE_NAME, null, values);
                if (rowID > 0){
                    rowUri = ContentUris.withAppendedId(TackleContract.ListItems.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return rowUri;
                }
            case TackleContract.Reminders.REMINDERS:
                rowID = db.insert(TackleContract.Reminders.TABLE_NAME, null, values);
                if (rowID > 0){
                    rowUri = ContentUris.withAppendedId(TackleContract.Reminders.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                    return rowUri;
                }

                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDB.getWritableDatabase();
        int rowsAffected = 0;
        String id;

        switch (uriType) {
            case TackleContract.TackleEvent.TACKLE_ITEMS:
                rowsAffected = db.delete(TackleContract.TackleEvent.TABLE_NAME, selection, selectionArgs);
                break;
            case TackleContract.TackleEvent.TACKLE_ITEM_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    String where = TackleContract.TackleEvent._ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.TackleEvent.TABLE_NAME, where, null);
                }
                else {
                    String where = selection + " AND " + TackleContract.TackleEvent._ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.TackleEvent.TABLE_NAME, where, null);
                }
                break;
            case TackleContract.Categories.CATEGORIES:
                rowsAffected = db.delete(TackleContract.Categories.TABLE_NAME, selection, selectionArgs);
                break;
            case TackleContract.Categories.CATEGORY_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    String where = TackleContract.Categories._ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.Categories.TABLE_NAME, where, null);
                }
                else {
                    String where = selection + " AND " + TackleContract.Categories._ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.Categories.TABLE_NAME, where, null);
                }
                break;
            case  TackleContract.ListItems.LIST_ITEMS:
                rowsAffected = db.delete(TackleContract.ListItems.TABLE_NAME, selection, selectionArgs);
                break;
            case TackleContract.ListItems.LIST_ITEM_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    String where = TackleContract.ListItems.ID + "=" +  id;
                    rowsAffected = db.delete(TackleContract.ListItems.TABLE_NAME, where, null);
                }
                else {
                    String where = selection + " AND " + TackleContract.ListItems.ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.ListItems.TABLE_NAME, where, null);
                }
                break;
            case TackleContract.Reminders.REMINDERS:
                rowsAffected = db.delete(TackleContract.Reminders.TABLE_NAME, selection, selectionArgs);
                break;
            case TackleContract.Reminders.REMINDER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    String where = TackleContract.Reminders.ID + "=" +  id;
                    rowsAffected = db.delete(TackleContract.Reminders.TABLE_NAME, where, null);
                }
                else {
                    String where = selection + " AND " + TackleContract.Reminders.ID + "=" + id;
                    rowsAffected = db.delete(TackleContract.Reminders.TABLE_NAME, where, null);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDB.getWritableDatabase();
        int rowsUpdated = 0;
        String id;


        switch (uriType) {
            case TackleContract.TackleEvent.TACKLE_ITEMS:
                rowsUpdated = db.update(TackleContract.TackleEvent.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TackleContract.TackleEvent.TACKLE_ITEM_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    db.update(TackleContract.TackleEvent.TABLE_NAME, values, TackleContract.TackleEvent._ID + "=" + id, null);
                } else {
                    db.update(TackleContract.TackleEvent.TABLE_NAME, values, TackleContract.TackleEvent._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case TackleContract.Categories.CATEGORIES:
                rowsUpdated = db.update(TackleContract.Categories.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TackleContract.Categories.CATEGORY_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    db.update(TackleContract.Categories.TABLE_NAME, values, TackleContract.Categories._ID + "=" + id, null);
                } else {
                    db.update(TackleContract.Categories.TABLE_NAME, values, TackleContract.Categories._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case TackleContract.ListItems.LIST_ITEMS:
                rowsUpdated = db.update(TackleContract.ListItems.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TackleContract.ListItems.LIST_ITEM_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    db.update(TackleContract.ListItems.TABLE_NAME, values, TackleContract.ListItems.ID + "=" + id, null);
                } else {
                    db.update(TackleContract.ListItems.TABLE_NAME, values, TackleContract.ListItems.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case TackleContract.Reminders.REMINDERS:
                rowsUpdated = db.update(TackleContract.Reminders.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TackleContract.Reminders.REMINDER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    db.update(TackleContract.Reminders.TABLE_NAME, values, TackleContract.Reminders.ID + "=" + id, null);
                } else {
                    db.update(TackleContract.Reminders.TABLE_NAME, values, TackleContract.Reminders.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
