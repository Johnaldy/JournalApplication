package com.example.android.journalapplication.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.journalapplication.DisplayActivity;
import com.example.android.journalapplication.R;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalAdapterViewHolder> {


    private final Context mContext;


    final private JournalAdapterOnClickHandler mClickHandler;


    public interface JournalAdapterOnClickHandler {
        void onClick(String date);
    }

    private Cursor mCursor;

    public JournalAdapter(Context context, JournalAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public JournalAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_items,
                viewGroup, false);

        view.setFocusable(true);
        return new JournalAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapterViewHolder journalAdapterViewHolder, int i) {

        mCursor.moveToPosition(i);

        String dateString = mCursor.getString(DisplayActivity.INDEX_JOURNAL_DATE);
        JournalAdapterViewHolder.dateView.setText(dateString);

        String titleString = mCursor.getString(DisplayActivity.INDEX_JOURNAL_TITLE);
        JournalAdapterViewHolder.titleView.setText(titleString);

        String contentString = mCursor.getString(DisplayActivity.INDEX_JOURNAL_CONTENT);
        JournalAdapterViewHolder.contentView.setText(contentString);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class JournalAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView dateView;
        final TextView contentView;
        final TextView titleView;

        public JournalAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            contentView = itemView.findViewById(R.id.contentTextView);
            dateView = itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String dateString = mCursor.getString(DisplayActivity.INDEX_JOURNAL_DATE);
            mClickHandler.onClick(dateString);


        }
    }
}
