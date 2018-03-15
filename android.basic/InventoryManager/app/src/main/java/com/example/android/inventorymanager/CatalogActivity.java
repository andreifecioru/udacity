package com.example.android.inventorymanager;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventorymanager.data.ProductBDHelper;
import com.example.android.inventorymanager.data.ProductContract;
import com.example.android.inventorymanager.models.Product;
import com.example.android.inventorymanager.models.Products;

import com.example.android.inventorymanager.data.ProductContract.ProductEntry;


public class CatalogActivity
        extends AppCompatActivity
        implements ProductCursorAdapter.OnSellProduct,
                   LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    private static final int PRODUCT_CURSOR_LOADER_ID = 1;

    private ProductCursorAdapter mProductCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup the list view to show the products inventory
        ListView productsListView = findViewById(R.id.products_list_view);
        productsListView.setEmptyView(findViewById(R.id.empty_view));

        // Setup the adapter for our list view
        mProductCursorAdapter = new ProductCursorAdapter(this, null, this);
        productsListView.setAdapter(mProductCursorAdapter);
        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected product with ID: " + id);

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI_PRODUCTS, id));

                startActivity(intent);
            }
        });

        // Setup the FAB to open the editor activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Setup the cursor loader
        getSupportLoaderManager()
                .initLoader(PRODUCT_CURSOR_LOADER_ID, null, this)
                .forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the app-bar menu
        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProducts(Products.generateDummyProducts());
                return true;
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Private helper methods
     */

    private void insertProducts(List<Product> products) {
        for (Product product : products) {
            insertProduct(product);
        }
    }

    private Uri insertProduct(Product product) {
        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPriceInCents());
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, product.getSupplierName());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, product.getSupplierPhone());

        Uri newProductUri = resolver.insert(ProductEntry.CONTENT_URI_PRODUCTS, values);
        if (newProductUri != null) {
            Log.d(LOG_TAG, "URI for new product: " + newProductUri);
        }

        return newProductUri;
    }

    private void deleteAllProducts() {
        ContentResolver resolver = getContentResolver();
        resolver.delete(ProductEntry.CONTENT_URI_PRODUCTS, null, null);
    }

    /**
     * Loader manager callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(CatalogActivity.this, ProductEntry.CONTENT_URI_PRODUCTS,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProductCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductCursorAdapter.swapCursor(null);

    }

    /**
     * Implements the "product-sell" functionality triggered when the user taps the "$" button in
     * one of the items in the list view.
     */
    @Override
    public void sell(Product product) {
        if (product.getQuantity() > 0) { // Only perform the sale if we have something in stock
            // Compute the product after applying the sale
            product = Products.productAfterSale(product);

            // Update the product in DB
            ContentResolver resolver = getContentResolver();

            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPriceInCents());
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, product.getSupplierName());
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, product.getSupplierPhone());

            Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI_PRODUCTS, product.getId());
            String selection = "_id=?";
            String[] selectionArgs = new String[] { String.valueOf(product.getId()) };
            int rowsUpdated = resolver.update(productUri, values, selection, selectionArgs);

            if (rowsUpdated > 0) {
                Toast.makeText(this, getString(R.string.msg_sell_product_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.msg_sell_product_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
