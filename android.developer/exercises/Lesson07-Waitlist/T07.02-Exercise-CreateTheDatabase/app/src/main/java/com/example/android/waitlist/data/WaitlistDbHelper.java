package com.example.android.waitlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.waitlist.data.WaitlistContract.WaitlistEntry;

public class WaitlistDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "waitlist.db";

    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " +
            WaitlistEntry.TABLE_NAME + " (" +
            WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
            WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
            WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

    private static final String SQL_DROP_TABLE = "DROP TABLE " +
            WaitlistEntry.TABLE_NAME + ";";

    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }
}