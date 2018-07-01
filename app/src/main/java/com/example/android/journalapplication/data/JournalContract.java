package com.example.android.journalapplication.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.android.journalapplication.data.JournalContract.JournalEntry.CONTENT_URI;

public final class JournalContract {

    private JournalContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.journalapp";

    //create the base of all URI's that apps will use to contact
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +
            CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_JOURNAL = "journal";

    public static final class JournalEntry implements BaseColumns {
        //URI to access the pet data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_JOURNAL);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of entries.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_JOURNAL;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single entry.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_JOURNAL;


        public final static String TABLE_NAME = "journal";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_CONTENT = "content";


        public static Uri buildJournalUriWithDate(String date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(date)
                    .build();
        }
    }


}
