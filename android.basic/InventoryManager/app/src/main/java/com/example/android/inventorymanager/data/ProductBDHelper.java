package com.example.android.inventorymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventorymanager.data.ProductContract.ProductEntry;

/*
 * DB helper class for interacting with the "product" table.
 */
final public class ProductBDHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    // DB file name on disk
    private static final String DATABASE_NAME = "inventory.db";

    // SQL statements as string constants
    // Create table schema
    private static final String SQL_CREATE_SCHEMA = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
            ProductEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0, " +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
            ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT NOT NULL" +
            ");";

    // Destroy the schema
    private static final String SQL_DESTROY_SCHEMA = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME + ";";

    public ProductBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table schema
        db.execSQL(SQL_CREATE_SCHEMA);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apply the same "destructive" procedure as we do in 'onUpgrade'
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Just replace the schema (loosing data in the process)
        db.execSQL(SQL_DESTROY_SCHEMA);
        onCreate(db);
    }
}
