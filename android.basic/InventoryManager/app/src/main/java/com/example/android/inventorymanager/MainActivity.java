package com.example.android.inventorymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.inventorymanager.data.ProductBDHelper;
import com.example.android.inventorymanager.models.Product;
import com.example.android.inventorymanager.models.Products;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView mDisplayTextView;

    private ProductBDHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayTextView = findViewById(R.id.display_text_view);

        mDbHelper = new ProductBDHelper(this);

        // put some data in DB
        resetAndSeedDatabase();

        // show the contents of the DB on the screen
        displayDbContents();
    }

    /*
     * Dumps the current contents of the DB onto the screen.
     */
    private void displayDbContents() {
        // sanity check
        if (mDisplayTextView == null) return;
        if (mDbHelper == null) return;

        List<Product> allProducts = mDbHelper.getAllProducts();

        mDisplayTextView.setText("Number of products in DB: " + allProducts.size());

        mDisplayTextView.append("\n\n_id - name - price - quantity - sup_name - sup_phone");

        for (Product product : allProducts) {
            mDisplayTextView.append("\n" + product);
        }
    }

    /*
     * Clears the contents of the DB and injects some hard-coded dummy records into the DB.
     */
    private void resetAndSeedDatabase() {
        // sanity check
        if (mDbHelper == null) return;

        // clear the database
        mDbHelper.deleteAllProducts();

        // add some seed data
        List<Product> seedData = Products.generateDummyProducts();

        for (Product product : seedData) {
            Product insertedProduct = mDbHelper.insertProduct(product);
            if (insertedProduct != null) {
                Log.d(LOG_TAG, "Successfully inserted product with ID: " + insertedProduct.getId());
            } else {
                Log.d(LOG_TAG, "Failed to insert new product in DB.");
            }
        }
    }
}
