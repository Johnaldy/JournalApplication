package com.example.android.journalapplication;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.journalapplication.data.CustomAdapter;
import com.example.android.journalapplication.data.JournalContract;
import com.example.android.journalapplication.data.JournalContract.JournalEntry;

import java.util.ArrayList;

import com.example.android.journalapplication.data.SetterGetter;
import com.example.android.journalapplication.recyclerview.JournalAdapter;

public class DisplayActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        JournalAdapter.JournalAdapterOnClickHandler {

    private final String TAG = DisplayActivity.class.getSimpleName();

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


    private static final int JOURNAL_LOADER = 44;
    private JournalAdapter mJournalAdapter;
    private RecyclerView mRecyclerView;
    private GoogleLogActivity googleLogActivity;
    private int mPosition = RecyclerView.NO_POSITION;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        getSupportActionBar().setElevation(0f);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, AddEntryActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.recycler_journal);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mJournalAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mJournalAdapter);

        getSupportLoaderManager().initLoader(JOURNAL_LOADER, null, this);

        googleLogActivity = new GoogleLogActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                googleLogActivity.revokeAccess();
                return true;

            case R.id.action_delete:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch (i) {

            case JOURNAL_LOADER:
                Uri journalQueryUri = JournalEntry.CONTENT_URI;

                return new CursorLoader(this,
                        journalQueryUri,
                        MAIN_JOURNAL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + i);
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mJournalAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mJournalAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String date) {
        Intent detailActivityIntent = new Intent(DisplayActivity.this, DetailActivity.class);
        Uri uriForDateClicked = JournalEntry.buildJournalUriWithDate(date);
        detailActivityIntent.setData(uriForDateClicked);
        startActivity(detailActivityIntent);
    }

}


