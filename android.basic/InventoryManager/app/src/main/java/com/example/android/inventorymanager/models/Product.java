package com.example.android.inventorymanager.models;


import android.text.TextUtils;

import java.util.Locale;

/*
 * Implements a 'model' class for the product entries in DB.
 *
 * A simple read-only POJO with attributes that correspond with the columns in the
 * 'product' table.
 */
final public class Product {
    private final static long DEFAULT_PRODUCT_ID = 0L;

    private final long mId;
    private final String mName;
    private final int mQuantity;
    private final String mSupplierName;
    private final String mSupplierPhone;

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor is used to create a product after we have the ID as a result of a DB insert.
     */
    public Product(long id, String name, int quantity, String supplierName, String supplierPhone) {
        // enforce invariants
        if (id < 0) {
            throw new IllegalArgumentException("Product ID must be a positive integer. Got: " + id);
        }

        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Product name must not be null or empty.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Product quantity must be a positive integer. Got: " + quantity);
        }

        if (TextUtils.isEmpty(supplierName)) {
            throw new IllegalArgumentException("Supplier name must not be null or empty.");
        }

        if (TextUtils.isEmpty(supplierPhone)) {
            throw new IllegalArgumentException("Supplier phone must not be null or empty.");
        }

        mId = id;
        mName = name;
        mQuantity = quantity;
        mSupplierName = supplierName;
        mSupplierPhone = supplierPhone;
    }

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor is used to create a product before we have the ID (before we do a DB insert).
     */
    Product(String name, int quantity, String supplierName, String supplierPhone) {
        this(DEFAULT_PRODUCT_ID, name, quantity, supplierName, supplierPhone);
    }

    /*
     * Clone the current product (only the ID is changed).
     *
     * Acts as a copy-constructor.
     */
    public Product cloneWithId(long id) {
        return new Product(id, mName, mQuantity, mSupplierName, mSupplierPhone);
    }

    /* Getters */
    public long getId() { return mId; }

    public String getName() { return mName; }

    public int getQuantity() { return mQuantity; }

    public String getSupplierName() { return mSupplierName; }

    public String getSupplierPhone() { return mSupplierPhone; }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "\n %d - %s - %d - %s -%s",
                mId, mName, mQuantity, mSupplierName, mSupplierPhone);
    }
}
