package com.example.android.inventorymanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventorymanager.models.Product;
import com.example.android.inventorymanager.models.Products;

import java.util.Locale;


public class ProductCursorAdapter extends CursorAdapter {
    private final static String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    private OnSellProduct mOnSellProduct;

    ProductCursorAdapter(Context context, Cursor cursor, OnSellProduct onSellProduct) {
        super(context, cursor, 0);
        mOnSellProduct = onSellProduct;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView  = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.mProduct = Products.fromCursor(cursor);
        viewHolder.nameTextView = rootView.findViewById(R.id.name_text_view);
        viewHolder.priceTextView = rootView.findViewById(R.id.price_text_view);
        viewHolder.quantityTextView = rootView.findViewById(R.id.quantity_text_view);
        viewHolder.mSellButton = rootView.findViewById(R.id.sell_button);

        // Hide the sell product button if the stock is empty.
        if (viewHolder.mProduct.getQuantity() > 0) {
            viewHolder.mSellButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mSellButton.setVisibility(View.GONE);
        }

        // Setup the "sell" button to initiate the sale of a product when tapped.
        // The adapter does not deal with the actual sale (i.e. updates in the DB).
        // We let the CatalogActivity to deal with that.
        viewHolder.mSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Selling product: " + viewHolder.mProduct.getName());
                mOnSellProduct.sell(viewHolder.mProduct);
            }
        });

        rootView.setTag(viewHolder);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Product product = Products.fromCursor(cursor);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.mProduct = product;
        viewHolder.nameTextView.setText(product.getName());
        viewHolder.priceTextView.setText(context.getString(R.string.product_item_price, product.getPrice()));
        viewHolder.quantityTextView.setText(context.getString(R.string.product_item_quantity, product.getQuantity()));

        // Hide the sell product button if the stock is empty.
        if (viewHolder.mProduct.getQuantity() > 0) {
            viewHolder.mSellButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mSellButton.setVisibility(View.GONE);
        }
    }

    private final class ViewHolder {
        Product mProduct;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button mSellButton;
    }

    /**
     * This interface defines the protocol for initiating a product sale.
     *
     * This is implemented by the {@link CatalogActivity} to do the actual sale (i.e. update the DB).
     */
    public interface OnSellProduct {
        void sell(Product product);
    }
}
