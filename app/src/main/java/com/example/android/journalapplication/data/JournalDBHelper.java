package com.example.android.journalapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.journalapplication.data.JournalContract.JournalEntry;

public class JournalDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Journal.db";

    public JournalDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //To Create database
       String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE " + JournalEntry.TABLE_NAME + "("
                + JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + JournalEntry.COLUMN_DATE + " INTEGER, "
                + JournalEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + JournalEntry.COLUMN_CONTENT + " TEXT NOT NULL );";

       db.execSQL(SQL_CREATE_JOURNAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
