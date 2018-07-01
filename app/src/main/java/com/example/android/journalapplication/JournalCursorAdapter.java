package com.example.android.journalapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.journalapplication.data.JournalContract.JournalEntry;

public class JournalCursorAdapter extends CursorAdapter {

    public JournalCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_items, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //find textViews to bind data
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);

        int titleColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DATE);
        int contentColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_CONTENT);

        //read the entry data from the cursor
        String journalTitle = cursor.getString(titleColumnIndex);
        String journalDate = cursor.getString(dateColumnIndex);
        String journalContent = cursor.getString(contentColumnIndex);

        //Update UI with details
        titleTextView.setText(journalTitle);
        dateTextView.setText(journalDate);
        contentTextView.setText(journalContent);
    }
}
