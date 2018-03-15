package com.example.android.inventorymanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.inventorymanager.data.ProductContract.ProductEntry;


public class ProductProvider extends ContentProvider {
    static final private String LOG_TAG = ProductProvider.class.getSimpleName();

    private ProductBDHelper mDbHelper;

    // URI route patterns
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int URI_PATTERN_PRODUCTS = 1000;
    private static final int URI_PATTERN_PRODUCTS_ID = 1001;

    // setup URI routes
    static {
        final UriMatcher matcher = sUriMatcher;

        matcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, URI_PATTERN_PRODUCTS);
        matcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT_ID, URI_PATTERN_PRODUCTS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductBDHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        // get a handle to a "read-only" DB
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // adjust the selection for our cursor based on the matched URI
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_PRODUCTS:
                // nothing to do here; just use the input params to get a cursor
                break;
            case URI_PATTERN_PRODUCTS_ID:
                // narrow-down the selection to a particular product
                long productId = ContentUris.parseId(uri);
                selection = ProductEntry.COLUMN_PRODUCT_ID + "=?";
                selectionArgs = new String[] { String.valueOf(productId) };
                break;
            default:
                throw new IllegalArgumentException("Query not supported for URI: " + uri);
        }

        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        // setup notifications (for our cursor loader)
        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // return the correct mime-type based on the matched URI
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_PRODUCTS:
                return ProductEntry.MIME_TYPE_PRODUCT_LIST;
            case URI_PATTERN_PRODUCTS_ID:
                return ProductEntry.MIME_TYPE_PRODUCT_ITEM;
            default:
                throw new IllegalArgumentException("Mime type is not supported for URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {
        // insertion is only work on the URI targeting the whole table
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_PRODUCTS:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        // perform deletion based on the matched URI
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_PRODUCTS:
                // nothing to do here; just use the input params to perform the deletion
                break;
            case URI_PATTERN_PRODUCTS_ID:
                // narrow-down the selection to a particular product
                long productId = ContentUris.parseId(uri);
                selection = ProductEntry.COLUMN_PRODUCT_ID + "=?";
                selectionArgs = new String[] { String.valueOf(productId) };
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for URI: " + uri);
        }

        return deleteProducts(uri, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        // perform updates based on the matched URI
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_PRODUCTS:
                // nothing to do here; just use the input params to perform the update
                break;
            case URI_PATTERN_PRODUCTS_ID:
                // narrow-down the selection to a particular product
                long productId = ContentUris.parseId(uri);
                selection = ProductEntry.COLUMN_PRODUCT_ID + "=?";
                selectionArgs = new String[] { String.valueOf(productId) };
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for URI: " + uri);
        }

        return updateProducts(uri, values, selection, selectionArgs);
    }

    /**
     * Helper methods
     */
    private Uri insertProduct(Uri uri, ContentValues values) {
        // Validate input values before making changes to DB
        validateProductDataOnInsert(values);

        // Get a handle to a "read-write" DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Try to insert the new product into the DB
        long newId = db.insert(ProductEntry.TABLE_NAME, null, values);
        if (newId != -1) {
            Log.d(LOG_TAG, "Insert new product in DB (id: " + newId + ")");
            notifyCursorChanged(uri);

            return ContentUris.withAppendedId(uri, newId);
        } else {
            Log.e(LOG_TAG, "Failed to add new product to the DB");
            return null;
        }
    }

    private int updateProducts(Uri uri,
                               ContentValues values,
                               String selection,
                               String[] selectionArgs) {
        // Let's see if there's anything to update (early exit)
        if (values.size() == 0) return 0;

        // Validate input values before making changes to DB
        validateProductDataOnUpdate(values);

        // Get a handle to a "read-write" DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Try to update the product in the DB
        int rowsUpdated = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated > 0) {
            Log.d(LOG_TAG, "Product updated in the DB.");
            notifyCursorChanged(uri);
        } else {
            Log.e(LOG_TAG, "Failed to update product in the DB.");
        }

        return rowsUpdated;
    }

    private int deleteProducts(Uri uri,
                               String selection,
                               String[] selectionArgs) {
        // Get a handle to a "read-write" DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Try to delete the product from the DB
        int rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted > 0) {
            Log.d(LOG_TAG, "Product deleted from DB.");
            notifyCursorChanged(uri);
        } else {
            Log.e(LOG_TAG, "Failed to delete product from DB.");
        }

        return rowsDeleted;
    }

    private void notifyCursorChanged(Uri uri) {
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    private void validateProductDataOnInsert(ContentValues values) {
        validateProductName(values, false);
        validateProductPrice(values, false);
        validateProductQuantity(values, false);
        validateProductSupplierName(values, false);
        validateProductSupplierPhone(values, false);
    }

    private void validateProductDataOnUpdate(ContentValues values) {
        validateProductName(values, true);
        validateProductPrice(values,true);
        validateProductQuantity(values, true);
        validateProductSupplierName(values, true);
        validateProductSupplierPhone(values, true);
    }

    /**
     * The methods below validate the product data contained within a {@link ContentValues} instance.
     * Based on the boolean flag it may first check if the specific key we're interested in exists or not.
     *
     * This makes these methods usable for both "insert" and "update" validation workflows.
     */
    private void validateProductName(ContentValues values, Boolean ifKeyExists) {
        // early exit if we want the key to actually exist in 'values', but it doesn't
        if (ifKeyExists && !values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) return;

        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Product must have a name");
        }
    }

    private void validateProductPrice(ContentValues values, Boolean ifKeyExists) {
        // early exit if we want the key to actually exist in 'values', but it doesn't
        if (ifKeyExists && !values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) return;

        int price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price < 0) {
            throw new IllegalArgumentException("Product price must be a positive integer");
        }
    }

    private void validateProductQuantity(ContentValues values, Boolean ifKeyExists) {
        // early exit if we want the key to actually exist in 'values', but it doesn't
        if (ifKeyExists && !values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) return;

        int quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity < 0){
            throw new IllegalArgumentException("Product quantity must be a positive integer");
        }
    }

    private void validateProductSupplierName(ContentValues values, Boolean ifKeyExists) {
        // early exit if we want the key to actually exist in 'values', but it doesn't
        if (ifKeyExists && !values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)) return;

        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Product must have a supplier name");
        }
    }

    private void validateProductSupplierPhone(ContentValues values, Boolean ifKeyExists) {
        // early exit if we want the key to actually exist in 'values', but it doesn't
        if (ifKeyExists && !values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE)) return;

        String phone = values.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);
        if (TextUtils.isEmpty(phone)) {
            throw new IllegalArgumentException("Product must have a supplier phone");
        }
    }
}
