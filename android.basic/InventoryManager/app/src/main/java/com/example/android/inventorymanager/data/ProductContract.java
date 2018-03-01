package com.example.android.inventorymanager.data;


import android.provider.BaseColumns;
/*
 * Implements the DB contract for the 'product' table.
 */
final public class ProductContract implements BaseColumns {
    // private constructor (prevents direct instantiation)
    private ProductContract() {}

    // contract definition
    public static final class ProductEntry {
        // the table name
        public static final String TABLE_NAME = "product";

        // column names
        public static final String COLUMN_PRODUCT_ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "sup_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "sup_phone";
    }
}
