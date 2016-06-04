package com.hiphople.letheapp.bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to manage bookmark via SQLite DB
 */
public class BookmarkDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public BookmarkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookmarkContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BookmarkContract.SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    public long addBookmark(String title, String url) {
        ContentValues values = new ContentValues();
        values.put(BookmarkContract.Bookmark.COLUMN_NAME_TITLE, title);
        values.put(BookmarkContract.Bookmark.COLUMN_NAME_URL, url);

        long rowId = getWritableDatabase().insert(BookmarkContract.Bookmark.TABLE_NAME,
                null,
                values);

        return rowId;
    }

    public boolean removeBookmark(String url) {
        int result = getWritableDatabase().delete(
                BookmarkContract.Bookmark.TABLE_NAME,
                BookmarkContract.Bookmark.COLUMN_NAME_URL + "=?",
                new String[]{url});

        return (result > 0 ? true : false);
    }

    public boolean updateBookmarkTitle(String newTitle, String url) {
        ContentValues values = new ContentValues();
        values.put(BookmarkContract.Bookmark.COLUMN_NAME_TITLE, newTitle);

        String whereClause = BookmarkContract.Bookmark.COLUMN_NAME_URL + " LIKE ?";
        String[] whereArgs = {url};

        int result = getWritableDatabase().update(
                BookmarkContract.Bookmark.TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        return (result > 0 ? true : false);
    }

    public Cursor queryBookmarkByUrl(String url) {
        Cursor result = executeSelectQuery(
                BookmarkContract.Bookmark.TABLE_NAME,
                BookmarkContract.COLUMNS_TO_RETURN,
                BookmarkContract.Bookmark.COLUMN_NAME_URL + "=?",
                new String[]{url});

        return result;
    }

    public int getCount() {
        Cursor result = executeSelectQuery(
                BookmarkContract.Bookmark.TABLE_NAME,
                null,
                null,
                null);
        return result.getCount();
    }

    public BookmarkItem getBookmarkItem(String url) {
        Cursor result = executeSelectQuery(
                BookmarkContract.Bookmark.TABLE_NAME,
                BookmarkContract.COLUMNS_TO_RETURN,
                null,
                null);

        BookmarkItem item = null;
        while (result.moveToNext()) {
            String currentUrl = result.getString(result.getColumnIndex(
                    BookmarkContract.Bookmark.COLUMN_NAME_URL));
            if (url.equals(currentUrl)) {
                String currentTitle = result.getString(result.getColumnIndex(
                        BookmarkContract.Bookmark.COLUMN_NAME_TITLE));
                item = new BookmarkItem(currentTitle, currentUrl);
                break;
            }
        }

        return item;
    }

    public BookmarkItem getBookmarkItem(int rowId) {
        Cursor result = executeSelectQuery(
                BookmarkContract.Bookmark.TABLE_NAME,
                BookmarkContract.COLUMNS_TO_RETURN,
                null,
                null);

        result.moveToPosition(rowId);

        String title = result.getString(result.getColumnIndex(
                BookmarkContract.Bookmark.COLUMN_NAME_TITLE));
        String url = result.getString(result.getColumnIndex(
                BookmarkContract.Bookmark.COLUMN_NAME_URL));

        return new BookmarkItem(title, url);
    }

    private Cursor executeSelectQuery(String table,
                                      String[] columns,
                                      String selection,
                                      String[] selectionArgs) {
        Cursor cursor = getReadableDatabase().query(
                table,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }
}