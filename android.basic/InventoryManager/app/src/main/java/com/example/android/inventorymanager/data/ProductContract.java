package com.example.android.inventorymanager.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/*
 * Implements the DB contract for the 'product' table.
 */
final public class ProductContract implements BaseColumns {
    static final String CONTENT_AUTHORITY = "com.example.android.products";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_PRODUCTS = "products";
    static final String PATH_PRODUCT_ID = "products/#";

    // Private constructor (prevents direct instantiation)
    private ProductContract() {}

    // Contract definition
    public static final class ProductEntry {

        // The table name
        static final String TABLE_NAME = "products";

        // Column names
        public static final String COLUMN_PRODUCT_ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "sup_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "sup_phone";

        // Content provider contract definition
        public static final Uri CONTENT_URI_PRODUCTS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        static final Uri CONTENT_URI_PRODUCT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_ID);

        // Mime types
        static final String MIME_TYPE_PRODUCT_LIST =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        static final String MIME_TYPE_PRODUCT_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
    }
}
