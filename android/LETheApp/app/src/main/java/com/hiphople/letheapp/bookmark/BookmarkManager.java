package com.hiphople.letheapp.bookmark;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

/**
 * Bookmark Manager class to help the Application deal with bookmark data
 */
public class BookmarkManager {
    private String TAG = "BookmarkManager";

    private BookmarkDbHelper mDbHelper;
    private Context mContext;
    private static Activity mAct;

    private static BookmarkManager instance;

    public static BookmarkManager getInstance(Activity act) {
        if (instance == null) {
            instance = new BookmarkManager(act);
        }
        mAct = act;
        return instance;
    }

    private BookmarkManager(Activity act) {
        mDbHelper = new BookmarkDbHelper(act.getApplicationContext());
        mContext = act.getApplicationContext();
        mAct = act;
    }

    /**
     * Add the page to Favorites only if it is not added yet.
     * If it is already added, open Favorites Modifier
     *
     * @param title
     * @param url
     */
    public void manageBookmark(String title, String url) {
        if (isExistingInDb(url)) {
            BookmarkItem item = mDbHelper.getBookmarkItem(url);
            BookmarkModifierDialog dialog = new BookmarkModifierDialog(mAct, item);
            dialog.show();
        } else {
            long rowId = mDbHelper.addBookmark(title, url);
            Log.d(TAG, "page added to Bookmark : rowId -" + rowId);
            Toast.makeText(mContext, "This page is added to Bookmark", Toast.LENGTH_LONG).show();
        }
    }

    public int getCount() {
        return mDbHelper.getCount();
    }

    /**
     *
     * @param pos position of bookmarked page stored in ListView
     * @return BookmarkItem
     */
    public BookmarkItem getBookmarkItem(int pos) {
        return mDbHelper.getBookmarkItem(pos);
    }

    private boolean isExistingInDb(String url) {
        boolean isExisting = false;
        Cursor result = mDbHelper.queryBookmarkByUrl(url);
        if (result.getCount() > 0) {
            isExisting = true;
        }
        return isExisting;
    }
}
