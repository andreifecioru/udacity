package com.example.android.inventorymanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventorymanager.models.Product;
import com.example.android.inventorymanager.data.ProductContract.ProductEntry;


/**
 * Activity implementing the product add/update functionality.
 */
public class EditorActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();

    private static final int PRODUCT_CURSOR_LOADER_ID = 1;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;

    // UI controls allowing the user to enter data
    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private EditText mProductQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private Button mQuantityIncreaseButton;
    private Button mQuantityDecreaseButton;
    private Button mCallSupplierButton;

    private Uri mProductUri;

    // Keeps track of whether the user has changed any of the fields
    private boolean mProductHasChanged = false;

    // Common on-touch listener for all our UI controls
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // If the user touches the UI control, the product is marked ans changed
            mProductHasChanged = true;
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        if (intent != null) {
            mProductUri = intent.getData();
        }

        if (mProductUri != null) {
            setTitle(R.string.editor_activity_title_edit_product);

            invalidateOptionsMenu();

            // Setup the cursor loader
            getSupportLoaderManager()
                    .initLoader(PRODUCT_CURSOR_LOADER_ID, null, this)
                    .forceLoad();
        } else {
            setTitle(R.string.editor_activity_title_new_product);
        }

        Log.d(LOG_TAG, "Product URI: " + mProductUri);

        // Setup the UI controls we'll use to read user input
        mProductNameEditText = findViewById(R.id.product_name_edit_text);
        mProductPriceEditText = findViewById(R.id.product_price_edit_text);
        mProductQuantityEditText = findViewById(R.id.product_quantity_edit_text);
        mSupplierNameEditText = findViewById(R.id.supplier_name_edit_text);
        mSupplierPhoneEditText = findViewById(R.id.supplier_phone_edit_text);
        mQuantityDecreaseButton = findViewById(R.id.product_quantity_decrease_button);
        mQuantityIncreaseButton = findViewById(R.id.product_quantity_increase_button);
        mCallSupplierButton = findViewById(R.id.call_supplier_button);

        // Setup the touch listeners, so that we detect when
        // the user makes any changes to the product
        mProductNameEditText.setOnTouchListener(mOnTouchListener);
        mProductPriceEditText.setOnTouchListener(mOnTouchListener);
        mProductQuantityEditText.setOnTouchListener(mOnTouchListener);
        mSupplierNameEditText.setOnTouchListener(mOnTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mOnTouchListener);

        // Setup the click listeners for quantity increase/decrease buttons
        mQuantityDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current quantity from the corresponding UI control
                int currentQuantity = getQuantityFromUI();

                // Decrease it
                currentQuantity--;

                // Cap the result to zero
                if (currentQuantity < 0) {
                    currentQuantity = 0;
                }

                // Place the result back in the UI control
                mProductQuantityEditText.setText(String.valueOf(currentQuantity));
            }
        });

        mQuantityIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current quantity from the corresponding UI control
                int currentQuantity = getQuantityFromUI();

                // Increase it
                currentQuantity++;

                // Place the result back in the UI control
                mProductQuantityEditText.setText(String.valueOf(currentQuantity));
            }
        });

        // Setup the "call supplier" click handler
        mCallSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(supplierPhone)) {
                    if (ActivityCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditorActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {
                        callSupplier();
                    }
                } else {
                    Toast.makeText(EditorActivity.this, getString(R.string.msg_no_supplier_phone), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the app-bar menu
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // If we are in "insert" mode, do not show the "delete" entry in the app-bar menu
        if (mProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: // User selected the "save" menu entry
                Product newProduct = createProductFromUserInput();
                if (newProduct != null) {
                    saveProduct(createProductFromUserInput());
                    finish();
                }
                return true;

            case R.id.action_delete: // User selected the "delete" menu entry
                showDeleteConfirmationDialog(
                        // User confirmed product deletion
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct();
                                finish();
                            }
                        },

                        // User cancelled product deletion
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditorActivity.this,
                                        R.string.msg_delete_product_cancelled, Toast.LENGTH_SHORT)
                                    .show();
                            }
                        }
                );
                return true;

            case android.R.id.home: // User pressed the "up" arrow in the app-bar
                if (mProductHasChanged) { // User made some changes: show confirmation dialog
                    showUnsavedChangesDialog(
                            // User wants to discard all changes: navigate back to parent activity
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Navigate back to parent activity
                                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                                }
                            }
                    );
                } else { // No changes were made: navigate back to parent activity
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mProductHasChanged) { // User tries to leave the activity with unsaved changes
            showUnsavedChangesDialog(
                    // User wants to discard all changes: just finish the editor activity
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }
            );
        } else { // No changes are made to the product: just continue as usual
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    callSupplier();
                } else {
                    // hide the "call supplier" button if we don't have permissions to use the dialer app
                    mCallSupplierButton.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    /**
     * Loader manager callbacks
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, mProductUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 1) { // we expect our cursor to have exactly one entry
            cursor.moveToPosition(0); // go to the beginning

            String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
            String supplierName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
            String supplierPhone = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));

            fillUIFromProduct(new Product(name, price, quantity, supplierName, supplierPhone));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mProductPriceEditText.setText("");
        mProductQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    /***********************
     * Private helper methods
     */

    /** Populate the UI controls with the data contained by a {@link Product} */
    private void fillUIFromProduct(Product product) {
        mProductNameEditText.setText(product.getName());
        mProductPriceEditText.setText(String.valueOf(product.getPrice()));
        mProductQuantityEditText.setText(String.valueOf(product.getQuantity()));
        mSupplierNameEditText.setText(product.getSupplierName());
        mSupplierPhoneEditText.setText(product.getSupplierPhone());
    }

    /**
     * Builds and displays the confirmation dialog for discarding unsaved data.
     *
     * @param onDiscardClickListener Logic that is executed when the user chooses to discard data.
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener onDiscardClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, onDiscardClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user chose to dismiss this dialog. Just close it.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        builder.create().show();
    }

    /**
     * Builds and displays the confirmation dialog for discarding unsaved data.
     *
     * @param onOkClickListener Logic that is executed when the user chooses to delete the product.
     * @param onCancelListener Logic that is executed when the user chooses to cancel the deletion of the product.
     */
    private void showDeleteConfirmationDialog(
            DialogInterface.OnClickListener onOkClickListener,
            DialogInterface.OnClickListener onCancelListener ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage(R.string.msg_delete_product_entry);
        builder.setPositiveButton(R.string.delete_yes, onOkClickListener);
        builder.setNegativeButton(R.string.delete_no, onCancelListener);

        builder.create().show();
    }

    /**
     * Extracts the product quantity from the corresponding UI control.
     */
    private int getQuantityFromUI() {
        String quantityAsString = mProductQuantityEditText.getText().toString().trim();
        int quantity = 0;

        if (!TextUtils.isEmpty(quantityAsString)) {
           quantity = Integer.parseInt(quantityAsString) ;
        }

        return quantity;
    }

    /**
     * Creates a {@link Product} instance from the user input. Also handles the data validation.
     *
     * @return A {@link Product} instance if UI data is valid; null otherwise.
     */
    private Product createProductFromUserInput() {
        try {
            String name = mProductNameEditText.getText().toString().trim();
            String priceAsString = mProductPriceEditText.getText().toString().trim();
            if (TextUtils.isEmpty(priceAsString)) {
                Toast.makeText(this, getString(R.string.invalid_user_input) + getString(R.string.invalid_product_price_empty), Toast.LENGTH_SHORT).show();
                return null;
            }
            double price = Double.parseDouble(priceAsString);

            String quantityAsString = mProductQuantityEditText.getText().toString().trim();
            if (TextUtils.isEmpty(quantityAsString)) {
                Toast.makeText(this, getString(R.string.invalid_user_input) + getString(R.string.invalid_product_quantity_empty), Toast.LENGTH_SHORT).show();
                return null;
            }
            int quantity = Integer.parseInt(quantityAsString);

            String supplierName = mSupplierNameEditText.getText().toString().trim();
            String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

            return new Product(name, (int) (price * 100), quantity, supplierName, supplierPhone);
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.invalid_user_input) + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void saveProduct(Product product) {
        // Sanity-check: early exit if product is null
        if (product == null) return;

        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPriceInCents());
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, product.getSupplierName());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, product.getSupplierPhone());

        if (mProductUri == null) { // "insert" mode
            Uri newProductUri = resolver.insert(ProductEntry.CONTENT_URI_PRODUCTS, values);
            if (newProductUri != null) {
                Log.d(LOG_TAG, "URI for new product: " + newProductUri);
            } else {
                Log.e(LOG_TAG, "Failed to save new product.");
            }
        } else { // "update" mode
            long productId = ContentUris.parseId(mProductUri);
            String selection = "_id=?";
            String[] selectionArgs = new String[] { String.valueOf(productId) };
            int rowsUpdated = resolver.update(mProductUri, values, selection, selectionArgs);

            if (rowsUpdated > 0) {
                Log.d(LOG_TAG, "Product updated.");
            } else {
                Log.e(LOG_TAG, "Failed to update product.");
            }
        }
    }

    private void deleteProduct() {
        ContentResolver resolver = getContentResolver();
        int deletedRows = resolver.delete(mProductUri, null, null);
        if (deletedRows > 0) {
            Log.d(LOG_TAG, "Product deleted.");
        } else {
            Log.e(LOG_TAG, "Failed to delete product");
        }
    }

    /**
     * Starts the dialler app activity to initiate a phone call with the supplier.
     *
     * At this point, all the logic around security permissions is complete.
     */
    private void callSupplier() {
        String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(EditorActivity.this, getString(R.string.msg_no_supplier_phone), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + supplierPhone));
            startActivity(intent);
        }
    }

}
