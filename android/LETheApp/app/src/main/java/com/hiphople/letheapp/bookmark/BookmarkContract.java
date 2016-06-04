package com.hiphople.letheapp.bookmark;

import android.provider.BaseColumns;

/**
 * Container for constants that define names for tables, columns and etc.
 */
public class BookmarkContract {

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    public static final String[] COLUMNS_TO_RETURN = {
            Bookmark._ID,
            Bookmark.COLUMN_NAME_TITLE,
            Bookmark.COLUMN_NAME_URL,
    };

    // SQL QUERIES
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Bookmark.TABLE_NAME + " (" +
                    Bookmark._ID + " INTEGER PRIMARY KEY," +
                    Bookmark.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    Bookmark.COLUMN_NAME_URL + TEXT_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Bookmark.TABLE_NAME;

    // not allow this class to be instantiated
    private BookmarkContract() {
    }

    /**
     * Inner class that defines the table contents
     */
    public static abstract class Bookmark implements BaseColumns {
        public static final String TABLE_NAME = "bookmark";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_URL = "url";
    }
}
