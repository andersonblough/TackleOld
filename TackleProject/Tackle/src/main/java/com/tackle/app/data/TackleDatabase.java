package com.tackle.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bill on 12/15/13.
 */
public class TackleDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tackle";
    private static final int DATABASE_VERSION = 24;

    private static final String DATABASE_CREATE_TACKLE_ITEMS = "create table " + TackleContract.TackleItems.TABLE_NAME + "("
            + TackleContract.TackleItems.ID + " integer primary key autoincrement, "
            + TackleContract.TackleItems.NAME + " text, "
            + TackleContract.TackleItems.TYPE + " integer, "
            + TackleContract.TackleItems.NOTES + " text, "
            + TackleContract.TackleItems.STATUS + " integer, "
            + TackleContract.TackleItems.TIMEZONE + " text, "
            + TackleContract.TackleItems.START_DATE + " integer, "
            + TackleContract.TackleItems.END_DATE + " integer, "
            + TackleContract.TackleItems.REMINDER_ID + " integer, "
            + TackleContract.TackleItems.CATEGORY_ID + " integer);";

    private static final String DATABASE_CREATE_CATEGORIES = "create table " + TackleContract.Categories.TABLE_NAME + "("
            + TackleContract.Categories.ID + " integer primary key autoincrement, "
            + TackleContract.Categories.NAME + " text, "
            + TackleContract.Categories.COLOR + " text);";

    private static final String DATABASE_CREATE_LIST_ITEMS = "create table " + TackleContract.ListItems.TABLE_NAME + "("
            + TackleContract.ListItems.ID + " integer primary key autoincrement, "
            + TackleContract.ListItems.NAME + " text, "
            + TackleContract.ListItems.QUANTITY + " integer, "
            + TackleContract.ListItems.STATUS + " integer, "
            + TackleContract.ListItems.LIST_ID + " integer);";

    private static final String DATABASE_CREATE_REMINDERS = "create table " + TackleContract.Reminders.TABLE_NAME + "("
            + TackleContract.Reminders.ID + " integer primary key autoincrement, "
            + TackleContract.Reminders.ITEM_ID + " integer, "
            + TackleContract.Reminders.TIMEZONE + " text, "
            + TackleContract.Reminders.DATE_TIME + " integer);";

    private static final String INITIALIZE_CATEGORIES = "INSERT INTO " + TackleContract.Categories.TABLE_NAME + "("
            + TackleContract.Categories.ID + "," +  TackleContract.Categories.NAME + "," + TackleContract.Categories.COLOR
            + ") VALUES(1, 'Inbox', '#CCCCCC')";

    public TackleDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_CATEGORIES);
        db.execSQL(DATABASE_CREATE_TACKLE_ITEMS);
        db.execSQL(DATABASE_CREATE_LIST_ITEMS);
        db.execSQL(DATABASE_CREATE_REMINDERS);

        // Set up database to have an INBOX Category
        db.execSQL("INSERT INTO categories(_id, name, color) VALUES(1, 'Inbox', '#CCCCCC')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*if (oldVersion < 24){
            // create temporary table to hold old data
            db.execSQL("CREATE TEMPORARY TABLE category_backup (_id INTEGER PRIMARY KEY AUTOINCREMENT,col_title TEXT,col_color TEXT);");
            db.execSQL("CREATE TEMPORARY TABLE list_backup (_id INTEGER PRIMARY KEY AUTOINCREMENT, col_title TEXT, col_qty INTEGER, col_completed INTEGER, col_list INTEGER);");
            db.execSQL("CREATE TEMPORARY TABLE reminder_backup (_id INTEGER PRIMARY KEY AUTOINCREMENT, col_datetime INTEGER, col_repeat INTEGER, col_event INTEGER);");
            db.execSQL("CREATE TEMPORARY TABLE todo_backup (_id INTEGER PRIMARY KEY AUTOINCREMENT, col_title TEXT, col_type INTEGER, col_category INTEGER, col_datetime INTEGER, col_reminder INTEGER);");

            // insert old data into temporary tables
            db.execSQL("INSERT INTO category_backup (_id, col_title, col_color) SELECT _id, name, color FROM categories");
            db.execSQL("INSERT INTO list_backup (_id, col_title, col_qty, col_completed, col_list) SELECT _id, title, quantity, completed, listID FROM list_items");
            db.execSQL("INSERT INTO reminder_backup (_id, col_datetime, col_repeat, col_event) SELECT _id, datetime, repeat, eventID FROM reminders");
            db.execSQL("INSERT INTO todo_backup (_id, col_title, col_type, col_category, col_datetime, col_reminder) SELECT _id, title, type, categoryID, datetime, reminderID FROM todos");

            // delete old tables
            db.execSQL("DROP TABLE IF EXISTS todos");
            db.execSQL("DROP TABLE IF EXISTS categories");
            db.execSQL("DROP TABLE IF EXISTS list_items");
            db.execSQL("DROP TABLE IF EXISTS reminders");

            // create new tables
            db.execSQL(DATABASE_CREATE_CATEGORIES);
            db.execSQL(DATABASE_CREATE_TACKLE_ITEMS);
            db.execSQL(DATABASE_CREATE_LIST_ITEMS);
            db.execSQL(DATABASE_CREATE_REMINDERS);

            // fill new tables with data
            db.execSQL("INSERT INTO " + TackleContract.Categories.TABLE_NAME + " ("
                + TackleContract.Categories.ID + ", "
                + TackleContract.Categories.NAME + ", "
                + TackleContract.Categories.COLOR + ") SELECT _id, col_title, col_color FROM category_backup");
            db.execSQL("INSERT INTO " + TackleContract.TackleItems.TABLE_NAME + " ("
                + TackleContract.TackleItems.ID + ", "
                + TackleContract.TackleItems.NAME + ", "
                + TackleContract.TackleItems.TYPE + ", "
                + TackleContract.TackleItems.CATEGORY_ID + ", "
                + TackleContract.TackleItems.START_DATE + ", "
                + TackleContract.TackleItems.REMINDER_ID + ") SELECT _id, col_title, col_type, col_category, col_datetime, col_reminder FROM todo_backup");
            db.execSQL("INSERT INTO " + TackleContract.ListItems.TABLE_NAME + " ("
                + TackleContract.ListItems.ID + ", "
                + TackleContract.ListItems.NAME + ", "
                + TackleContract.ListItems.QUANTITY + ", "
                + TackleContract.ListItems.STATUS + ", "
                + TackleContract.ListItems.LIST_ID + ") SELECT _id, col_title, col_qty, col_completed, col_list FROM list_backup");
            db.execSQL("INSERT INTO " + TackleContract.Reminders.TABLE_NAME + " ("
                + TackleContract.Reminders.ID + ", "
                + TackleContract.Reminders.DATE_TIME + ", "
                + TackleContract.Reminders.ITEM_ID + ") SELECT _id, col_datetime, col_event FROM reminder_backup");

            // delete the temporary tables
            db.execSQL("DROP TABLE IF EXISTS category_backup");
            db.execSQL("DROP TABLE IF EXISTS todo_backup");
            db.execSQL("DROP TABLE IF EXISTS list_backup");
            db.execSQL("DROP TABLE IF EXISTS reminder_backup");

        }*/

    }
}
