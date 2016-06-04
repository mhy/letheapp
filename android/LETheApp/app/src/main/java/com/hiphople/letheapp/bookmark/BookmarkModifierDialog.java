package com.hiphople.letheapp.bookmark;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiphople.letheapp.R;

/**
 * Dialog of Bookmark Modifier that can update title of a bookmarked page or delete the page.
 */
public class BookmarkModifierDialog extends Dialog
        implements View.OnClickListener {

    private static final String EMPTY_STRING = "";
    private final String WARNING_TITLE_CANNOT_BE_EMPTY;
    private BookmarkItem mItem;
    private BookmarkDbHelper mDbHelper;
    private EditText etBmTitle;

    public BookmarkModifierDialog(Context context, BookmarkItem item) {
        super(context);

        mItem = item;
        mDbHelper = new BookmarkDbHelper(context);
        WARNING_TITLE_CANNOT_BE_EMPTY = context.getString(R.string.bookmark_title_cannot_be_empty);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bookmark_modifier);
        etBmTitle = (EditText) findViewById(R.id.etBookmarkTitle);
        etBmTitle.setText(mItem.getTitle());
        TextView tvBmUrl = (TextView) findViewById(R.id.tvBookmarkUrl);
        tvBmUrl.setText(mItem.getUrl());

        Button btnChangeTitle = (Button) findViewById(R.id.btnBmChangeTitle);
        Button btnDelete = (Button) findViewById(R.id.btnBmDelete);
        Button btnCancel = (Button) findViewById(R.id.btnBmCancel);

        btnChangeTitle.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBmChangeTitle:
                if (etBmTitle == null || EMPTY_STRING.equals(etBmTitle.getText().toString())) {
                    Toast.makeText(getContext(), WARNING_TITLE_CANNOT_BE_EMPTY, Toast.LENGTH_SHORT).show();
                } else {
                    mDbHelper.updateBookmarkTitle(etBmTitle.getText().toString(), mItem.getUrl());
                    dismiss();
                }
                break;
            case R.id.btnBmDelete:
                mDbHelper.removeBookmark(mItem.getUrl());
                dismiss();
                break;
            case R.id.btnBmCancel:
                cancel();
                break;
        }
    }
}
