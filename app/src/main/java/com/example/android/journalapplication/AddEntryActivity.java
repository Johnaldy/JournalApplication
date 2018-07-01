package com.example.android.journalapplication;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.android.journalapplication.data.JournalContract.JournalEntry;

public class AddEntryActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor> {

    private static final int EXISTING_JOURNAL_LOADER = 0;

    private Uri mCurrentJournalUri;
    /**
     * EditText field to enter the date
     */
    private TextView mDateTextView;

    /**
     * EditText field to enter the diary title
     */
    private EditText mTitleEditText;

    /**
     * EditText field to enter user's content
     */
    private EditText mContentEditText;
    private boolean mJournalEdited = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mJournalEdited = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry_activity);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new entry or editing an existing one.
        Intent intent = getIntent();
        mCurrentJournalUri = intent.getData();

        // If the intent DOES NOT contain a entry content URI, then we know that we are
        // creating a new entry.
        if (mCurrentJournalUri == null) {
            // This is a new entry, so change the app bar to say "Add a entry"
            setTitle(getString(R.string.editor_activity_title_new_entry));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a entry that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing entry, so change app bar to say "Edit entry"
            setTitle(getString(R.string.editor_activity_title_edit));

            // Initialize a loader to read the entry data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_JOURNAL_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mDateTextView = (TextView) findViewById(R.id.dateEntry);
        mTitleEditText = (EditText) findViewById(R.id.titleEntry);
        mContentEditText = (EditText) findViewById(R.id.contentEntry);

        mDateTextView.setOnTouchListener(mTouchListener);
        mTitleEditText.setOnTouchListener(mTouchListener);
        mContentEditText.setOnTouchListener(mTouchListener);
    }

    /**
     * Get user input from editor and save new entry into database.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveJournalData() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space

        //Convert long to usable date (String)
        long dateNew = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss", Locale.getDefault());
        Date resultDate = new Date(dateNew);

        String dateString = sdf.format(resultDate);
        String titleString = mTitleEditText.getText().toString().trim();
        String contentString = mContentEditText.getText().toString().trim();

        //confirm fields are blank
        if (mCurrentJournalUri == null &&
                TextUtils.isEmpty(dateString) && TextUtils.isEmpty(titleString)
                && TextUtils.isEmpty(contentString)) {
            return;
        }


        // Create a ContentValues object where column names are the keys,
        // and entry attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(JournalEntry.COLUMN_DATE, dateString);
        values.put(JournalEntry.COLUMN_TITLE, titleString);
        values.put(JournalEntry.COLUMN_CONTENT, contentString);


        // Determine if this is a new or existing entry by checking if mCurrententryUri is null or not
        if (mCurrentJournalUri == null) {
            // This is a NEW entry, so insert a new entry into the provider,
            // returning the content URI for the new entry.
            Uri newUri = getContentResolver().insert(JournalEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_entry_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_entry_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING entry, so update the entry with content URI: mCurrententryUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrententryUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentJournalUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_entry_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_entry_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.edit_mode, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
        // If this is a new entry, hide the "Delete" menu item.
//        if (mCurrentJournalUri == null) {
//            MenuItem menuItem = menu.findItem(R.id.action_delete);
//            menuItem.setVisible(false);
//        }
//        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_done:
                // Save entry to database
                saveJournalData();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_sign_out:
                // Do nothing for now
                return true;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mJournalEdited) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddEntryActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (!mJournalEdited){
            super.onBackPressed();
        return;
        }

    // Create a click listener to handle the user confirming that changes should be discarded.
    DialogInterface.OnClickListener discardButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User clicked "Discard" button, close the current activity.
                    finish();
                }
            };

    // Show dialog that there are unsaved changes
    showUnsavedChangesDialog(discardButtonClickListener);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                JournalEntry._ID,
                JournalEntry.COLUMN_DATE,
                JournalEntry.COLUMN_CONTENT};

        return new CursorLoader(this,
                mCurrentJournalUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }
        try {
            cursor.moveToFirst();

        }catch (Exception e){

            int dateColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DATE);
            int titleColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_TITLE);
            int contentColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_CONTENT);

            String dateField = cursor.getString(dateColumnIndex);
            String titleField = cursor.getString(titleColumnIndex);
            String contentField = cursor.getString(contentColumnIndex);

            mDateTextView.setText(dateField);
            mTitleEditText.setText(titleField);
            mContentEditText.setText(contentField);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDateTextView.setText("");
        mTitleEditText.setText("");
        mContentEditText.setText("");

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the entry.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the entry.
                deleteEntry();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the entry.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteEntry() {
        // Only perform the delete if this is an existing entry.
        if (mCurrentJournalUri != null) {
            // Call the ContentResolver to delete the entry at the given content URI.
            // Pass in null for the selection and selection args because the mCurrententryUri
            // content URI already identifies the entrythat we want.
            int rowsDeleted = getContentResolver().delete(mCurrentJournalUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_entry_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_entry_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}