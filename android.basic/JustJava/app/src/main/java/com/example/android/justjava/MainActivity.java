package com.example.android.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final static int PRICE_PER_COFFEE = 5;
    private int quantity = 0;

    private int getQuantity() {
        if (quantity < 0) quantity = 0;
        return quantity;
    }

    private void setQuantity(int value) {
        if (value < 0) quantity = 0;
        else quantity = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {
        displayPrice(getQuantity() * PRICE_PER_COFFEE);
        Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_SHORT).show();
    }

    public void quantityIncrease(View view) {
        setQuantity(++quantity);
        displayQuantity(getQuantity());
    }

    public void quantityDecrease(View view) {
        setQuantity(--quantity);
        displayQuantity(getQuantity());
    }

    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format(Locale.ENGLISH,"%d", number));
    }

    private void displayPrice(int price) {
        TextView priceTextView = findViewById(R.id.price_text_view);
        priceTextView.setText(String.format("Total: %s\nThank you!", NumberFormat.getCurrencyInstance().format(price)));
    }
}
