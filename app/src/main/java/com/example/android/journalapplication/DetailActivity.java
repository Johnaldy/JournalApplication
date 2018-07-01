package com.example.android.journalapplication;



import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.journalapplication.data.JournalContract.JournalEntry;


public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = DetailActivity.class.getSimpleName();

    public static final String[] MAIN_JOURNAL_PROJECTION = {
            JournalEntry.COLUMN_DATE,
            JournalEntry.COLUMN_TITLE,
            JournalEntry.COLUMN_CONTENT,
            JournalEntry._ID
    };

    public static final int INDEX_JOURNAL_DATE = 0;
    public static final int INDEX_JOURNAL_TITLE = 1;
    public static final int INDEX_JOURNAL_CONTENT = 2;
    public static final int INDEX_JOURNAL_ID = 3;

    private static final int ID_DETAIL_LOADER = 353;


    private Uri mUri;

    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.edit_mode, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            //Fill in with activities
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        switch (i){
            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MAIN_JOURNAL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + i);

        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        String dateString = data.getString(INDEX_JOURNAL_DATE);
        mDetailBinding.dateTextViewDetail.setText(dateString);

        String titleString = data.getString(INDEX_JOURNAL_TITLE);
        mDetailBinding.titleTextViewDetail.setText(titleString);

        String contentString = data.getString(INDEX_JOURNAL_CONTENT);
        mDetailBinding.contentTextViewDetail.setText(contentString);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
