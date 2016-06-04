package com.hiphople.letheapp.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hiphople.letheapp.R;

/**
 * Activity that manage the list of the bookmarked pages.
 */
public class BookmarkActivity extends Activity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "BookmarkActivity";

    private ListView lvBookmark;
    private ListViewAdaptor mLvAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        lvBookmark = (ListView) findViewById(R.id.lvBookmark);
        mLvAdaptor = new ListViewAdaptor(getApplicationContext());
        lvBookmark.setAdapter(mLvAdaptor);
        lvBookmark.setOnItemClickListener(this);
        lvBookmark.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookmarkManager bm = BookmarkManager.getInstance(this);
        Intent i = getIntent();
        i.putExtra(BookmarkContract.Bookmark.COLUMN_NAME_URL, bm.getBookmarkItem(position).getUrl());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BookmarkManager bm = BookmarkManager.getInstance(this);
        BookmarkItem item = bm.getBookmarkItem(position);
        BookmarkModifierDialog dialog = new BookmarkModifierDialog(this, item);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLvAdaptor.notifyDataSetChanged();
            }
        });
        dialog.show();

        return true;
    }

    private class ListViewAdaptor extends BaseAdapter {
        BookmarkManager mBookmarkManager;
        LayoutInflater mInflater;

        ListViewAdaptor(Context context) {
            mBookmarkManager = BookmarkManager.getInstance(BookmarkActivity.this);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object getItem(int position) {
            return mBookmarkManager.getBookmarkItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.favorites_item, parent, false);
            }

            TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
            title.setText(mBookmarkManager.getBookmarkItem(position).getTitle());

            TextView url = (TextView) convertView.findViewById(R.id.tvUrl);
            url.setText(mBookmarkManager.getBookmarkItem(position).getUrl());

            return convertView;
        }

        @Override
        public int getCount() {
            return mBookmarkManager.getCount();
        }
    }
}
