package com.tackle.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bill on 12/15/13.
 */
public class TackleContract {

    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.tackle.app.data.TackleProvider";


    public interface TackleEventColumns {
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

    public interface CategoryColomns {
        public static final String CATEGORY_NAME = "cat_name";
        public static final String COLOR = "color";
    }

    public static final class Categories implements BaseColumns, CategoryColomns {
        public static final long DEFAULT_ID = 1;
        public static final String DEFAULT_TITLE = "Inbox";
        public static final String DEFAULT_COLOR = "#CCCCCC";

        public static final String TABLE_NAME = "categories";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int CATEGORIES = 100;
        public static final int CATEGORY_ID = 110;
    }

    public static final class TackleEvent implements BaseColumns, TackleEventColumns {
        public static void delete(ContentResolver contentResolver, long id) {
            Uri uri = ContentUris.withAppendedId(TackleEvent.CONTENT_URI, id);
            contentResolver.delete(uri, null, null);
            contentResolver.delete(ListItems.CONTENT_URI, ListItems.EVENT_ID + "=" + id, null);
            contentResolver.delete(Reminders.CONTENT_URI, Reminders.EVENT_ID + "=" + id, null);
        }

        public static final String TABLE_NAME = "tackle_events";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int TACKLE_ITEMS = 120;
        public static final int TACKLE_ITEM_ID = 130;

    }

    public static final class Reminders {
        public static final String TABLE_NAME = "reminders";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int REMINDERS = 140;
        public static final int REMINDER_ID = 150;

        public static final String ID = "_id";
        public static final String EVENT_ID = "event_id";
        public static final String MINUTES = "minutes";

    }

    public static final class ListItems {
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
        public static final String EVENT_ID = "event_id";
    }

    public static final class TackleInstances implements BaseColumns, TackleEventColumns, CategoryColomns {
        public static final String TABLE_NAME = "instances";

        private static final String BASE_PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + BASE_PATH);

        public static final String URI_TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String URI_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        public static final int INSTANCES = 180;
        public static final int INSTANCE_ID = 190;

        public static final String EVENT_ID = "event_id";
        public static final String BEGIN = "begin";
        public static final String END = "end";
        public static final String START_DAY = "start_day";
        public static final String START_MINUTE = "start_min";
        public static final String END_DAY = "end_day";
        public static final String END_MINUTE = "end_min";

    }
}
