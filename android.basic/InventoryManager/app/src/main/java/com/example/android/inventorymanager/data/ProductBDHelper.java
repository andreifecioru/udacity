package com.example.android.inventorymanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventorymanager.data.ProductContract.ProductEntry;
import com.example.android.inventorymanager.models.Product;

import java.util.ArrayList;
import java.util.List;

/*
 * DB helper class for interacting with the "product" table.
 */
final public class ProductBDHelper extends SQLiteOpenHelper {
    private static final int DATABSE_VERSION = 1;

    // DB file name on disk
    private static final String DATABASE_NAME = "inventory.db";

    // SQL statements as string constants
    // create table schema
    private static final String SQL_CREATE_SCHEMA = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
            ProductEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
            ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT NOT NULL" +
            ");";

    // destroy the schema
    private static final String SQL_DESTROY_SCHEMA = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME + ";";

    // truncate table (i.e. delete all entries)
    private static final String SQL_DELETE_ALL = "DELETE FROM " + ProductEntry.TABLE_NAME + ";";

    public ProductBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the table schema
        db.execSQL(SQL_CREATE_SCHEMA);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // apply the same "destructive" procedure as we do in 'onUpgrade'
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // just replace the schema (loosing data in the process)
        db.execSQL(SQL_DESTROY_SCHEMA);
        onCreate(db);
    }

    /*
     * Inserts a given product into the DB
     *
     * @return the inserted product (if insertion successful) with the updated ID (assigned by the
     *         DB engine at insertion time) or null if insertion failed.
     */
    public Product insertProduct(Product product) {
        ContentValues values = new ContentValues();

        // we don't provide the ID here. It will be automatically assigned during DB insertion
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, product.getSupplierName());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, product.getSupplierPhone());

        // obtain a 'writable' DB handler
        SQLiteDatabase db = getWritableDatabase();

        // perform the actual insert
        long newId = db.insert(ProductEntry.TABLE_NAME, null, values);
        if (newId != -1) { // success
            return product.cloneWithId(newId);
        } else { // failure
            return null;
        }
    }

    /*
     * Returns the list of products stored inside the DB.
     */
    public List<Product> getAllProducts() {
        // obtain a 'readable' DB handler
        SQLiteDatabase db = getReadableDatabase();

        // we don't actually need a projection, since we want all the columns
        String[] projection = {
                ProductEntry.COLUMN_PRODUCT_ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
        };

        // let's use the try-with-resources construct instead of using 'finally'
        try (Cursor cursor = db.query(ProductEntry.TABLE_NAME,
                    projection,    // since we select all columns we could have just passed null here
                    null, // we want all the rows
                    null,
                    null,
                    null,
                    null,
                    null)) {

            List<Product> result = new ArrayList<>();

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
                String supplierName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
                String supplierPhone = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));

                result.add(new Product(id, name, quantity, supplierName, supplierPhone));
            }

            return result;
        }
    }

    /*
     * Deletes all entries in the DB (i.e. truncates the table).
     */
    public void deleteAllProducts() {
        // obtain a 'writable' DB handler
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(SQL_DELETE_ALL);
    }
}
