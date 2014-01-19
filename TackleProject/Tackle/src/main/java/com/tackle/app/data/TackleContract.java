package com.tackle.app.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bill on 12/15/13.
 */
public class TackleContract {

    public interface TackleEventColumns{
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String TIMEZONE = "timezone";
        public static final String ALL_DAY = "all_day";
        public static final String FREQUENCY = "freq";
        public static final String BY_DAY = "by_day";
        public static final String UNTIL = "until";
        public static final String COUNT = "count";
        public static final String NOTES = "notes";
        public static final String CATEGORY_ID = "cat_id";
        public static final String STATUS = "status";
    }

    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.tackle.app.data.TackleProvider";

    public static final int TODO = 1;
    public static final int LIST = 2;
    public static final int EVENT = 4;
    public static final int NOTE = 3;


    public static final class Categories
    {
        public static final String TABLE_NAME = "categories";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int CATEGORIES = 100;
        public static final int CATEGORY_ID = 110;

        public static final String ID = "_id";
        public static final String NAME =  "name";
        public static final String COLOR = "color";
    }

    public static final class TackleItems
    {
        public static final String TABLE_NAME = "tackleitems";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int TACKLE_ITEMS = 120;
        public static final int TACKLE_ITEM_ID = 130;

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String NOTES = "notes";
        public static final String STATUS = "status";
        public static final String TIMEZONE = "timezone";
        public static final String START_DATE = "startDate";
        public static final String END_DATE = "endDate";
        public static final String REMINDER_ID = "reminderID";
        public static final String CATEGORY_ID = "categoryID";

    }

    public static final class Reminders
    {
        public static final String TABLE_NAME = "reminders";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int REMINDERS = 140;
        public static final int REMINDER_ID = 150;

        public static final String ID = "_id";
        public static final String ITEM_ID = "itemID";
        public static final String TIMEZONE = "timezone";
        public static final String DATE_TIME = "dateTime";

    }

    public static final class ListItems
    {
        public static final String TABLE_NAME = "listitems";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int LIST_ITEMS = 160;
        public static final int LIST_ITEM_ID = 170;

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String QUANTITY = "quantity";
        public static final String STATUS = "status";
        public static final String LIST_ID = "listID";
    }
}
