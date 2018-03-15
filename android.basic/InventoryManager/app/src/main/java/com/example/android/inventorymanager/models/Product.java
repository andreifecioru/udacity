package com.example.android.inventorymanager.models;


import android.content.Context;
import android.text.TextUtils;

import com.example.android.inventorymanager.InventoryManagerApplication;
import com.example.android.inventorymanager.R;

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
    private final double mPrice;
    private final int mQuantity;
    private final String mSupplierName;
    private final String mSupplierPhone;

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor is used to create a product after we have the ID as a result of a DB insert.
     */
    public Product(long id, String name, double price, int quantity, String supplierName, String supplierPhone) {
        // Get a hold of the application context to access the string resources
        Context appContext = InventoryManagerApplication.appContext;

        // Enforce invariants
        if (id < 0) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_product_id, id));
        }

        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_product_name));
        }

        if (price < 0) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_product_price, price));
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_product_quantity, quantity));
        }

        if (TextUtils.isEmpty(supplierName)) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_supplier_name));
        }

        if (TextUtils.isEmpty(supplierPhone)) {
            throw new IllegalArgumentException(appContext.getString(R.string.invalid_supplier_phone));
        }

        mId = id;
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mSupplierName = supplierName;
        mSupplierPhone = supplierPhone;
    }

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor is used to create a product before we have the ID (before we do a DB insert).
     */
    public Product(String name, double price, int quantity, String supplierName, String supplierPhone) {
        this(DEFAULT_PRODUCT_ID, name, price, quantity, supplierName, supplierPhone);
    }

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor allows the price param to be provided in cents (as integer)
     */
    public Product(long id, String name, int priceInCents, int quantity, String supplierName, String supplierPhone) {
        this(id, name, priceInCents / 100.0, quantity, supplierName, supplierPhone);
    }

    /*
     * Creates a @{link Product} instance (constructor)
     *
     * This constructor allows the price param to be provided in cents (as integer)
     * This constructor is used to create a product before we have the ID (before we do a DB insert).
     */
    public Product(String name, int priceInCents, int quantity, String supplierName, String supplierPhone) {
        this(DEFAULT_PRODUCT_ID, name, priceInCents, quantity, supplierName, supplierPhone);
    }

    /*
     * Clone the current product (only the ID is changed).
     *
     * Acts as a copy-constructor.
     */
    public Product cloneWithId(long id) {
        return new Product(id, mName, mPrice, mQuantity, mSupplierName, mSupplierPhone);
    }

    /* Getters */
    public long getId() { return mId; }

    public String getName() { return mName; }

    public double getPrice() { return mPrice; }

    /** Returns the product price in cents (as integer) */
    public double getPriceInCents() { return (int) (mPrice * 100); }

    public int getQuantity() { return mQuantity; }

    public String getSupplierName() { return mSupplierName; }

    public String getSupplierPhone() { return mSupplierPhone; }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "\n %d - %s - $%.2f - %d - %s -%s",
                mId, mName, mPrice, mQuantity, mSupplierName, mSupplierPhone);
    }
}
