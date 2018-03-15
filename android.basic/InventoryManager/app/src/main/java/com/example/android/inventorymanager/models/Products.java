package com.example.android.inventorymanager.models;


import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import com.example.android.inventorymanager.data.ProductContract.ProductEntry;

final public class Products {
    /**
     * Generates a list of "hard-coded" fake products. Useful for quickly seeding the DB.
     *
     * @return A {@link List<Product>} with a small set of fake products.
     */
    static public List<Product> generateDummyProducts() {
        List<Product> result = new ArrayList<>();

        result.add(new Product("Wooden chair", 120.00, 3, "The Carpenter's Shop", "800-088-22345"));
        result.add(new Product("Wooden table", 225.99, 5, "The Carpenter's Shop", "800-088-22345"));
        result.add(new Product("Metal bench", 299.99, 2, "The Iron Wizards", "800-900-12345"));
        result.add(new Product("Metal flower stand", 89.99, 8, "The Iron Wizards", "800-900-12345"));

        return result;
    }

    /**
     * Returns a {@link Product} after a sale is performed on it.
     *
     * @return A clone of the original {@link Product} with quantity reduced by one (capped to lower-bound zero)
     */
    static public Product productAfterSale(Product product) {
        // Sanity check: nothing left to sell: return the same product
        if (product.getQuantity() <= 0) return product;

        return new Product(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity() - 1,
                product.getSupplierName(),
                product.getSupplierPhone());
    }

    /**
     * Generates a {@link Product} from data extracted from a {@link Cursor}
     *
     * @return A {@link Product} with data extracted from the current position of the input {@link Cursor}
     */
    static public Product fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_ID));
        String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        String supplierName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
        String supplierPhone = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));

        return new Product(id,
            name,
            price,
            quantity,
            supplierName,
            supplierPhone);
    }
}
