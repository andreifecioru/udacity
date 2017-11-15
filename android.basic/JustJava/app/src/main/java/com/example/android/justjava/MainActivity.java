package com.example.android.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {
    private final static int PRICE_PER_COFFEE = 5;
    private final static int PRICE_VANILLA_TOPPING = 2;
    private final static int PRICE_MILK_TOPPING = 1;

    private int quantity = 0;

    @BindView(R.id.name_edit_text) EditText nameEditText;
    @BindView(R.id.vanilla_check_box) CheckBox vanillaToppingCheckBox;
    @BindView(R.id.milk_check_box) CheckBox milkToppingCheckBox;
    @BindView(R.id.quantity_text_view) TextView quantityTextView;
    @BindView(R.id.order_summary_text_view) TextView orderSummaryTextView;

    @BindString(R.string.place_your_order) String placeYourOrderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        quantityTextView.setText("0");
        orderSummaryTextView.setText(placeYourOrderText);
    }

    @OnClick(R.id.place_order_button)
    public void placeOrder(View view) {
        Toast.makeText(getApplicationContext(), "Your order has been placed.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.quantity_increase_button)
    public void quantityIncrease(View view) {
        setQuantity(++quantity);
        displayQuantity(getQuantity());
        displayOrderSummary();
    }

    @OnClick(R.id.quantity_decrease_button)
    public void quantityDecrease(View view) {
        setQuantity(--quantity);
        displayQuantity(getQuantity());
        displayOrderSummary();
    }

    @OnTextChanged(R.id.name_edit_text)
    @OnCheckedChanged({R.id.vanilla_check_box, R.id.milk_check_box})
    public void updateOrderSummary() {
        displayOrderSummary();
    }

    private int getQuantity() {
        if (quantity < 0) quantity = 0;
        return quantity;
    }

    private void setQuantity(int value) {
        if (value < 0) quantity = 0;
        else quantity = value;
    }

    private void displayQuantity(int number) {
        quantityTextView.setText(String.format(Locale.ENGLISH,"%d", number));
    }

    private int calculatePrice() {
        int pricePerCup = PRICE_PER_COFFEE;
        if (vanillaToppingCheckBox.isChecked())
            pricePerCup += PRICE_VANILLA_TOPPING;
        if (milkToppingCheckBox.isChecked())
            pricePerCup += PRICE_MILK_TOPPING;

        return pricePerCup * quantity;
    }

    private void displayOrderSummary() {
        if (quantity == 0) {
            orderSummaryTextView.setText(placeYourOrderText);
        } else {
            String name = nameEditText.getText().toString();
            if ("".equals(name))
                name = "John Doe";
            StringBuilder sb = new StringBuilder("");
            sb.append("Hi ").append(name).append("!\n");
            sb.append("You have ordered:\n");
            sb.append("  - ").append(quantity).append(" coffees\n");
            if (vanillaToppingCheckBox.isChecked())
                sb.append("  - vanilla topping\n");
            if (milkToppingCheckBox.isChecked())
                sb.append("  - milk topping\n");
            sb.append("Total: $").append(calculatePrice()).append("\n");
            sb.append("Thank you!");
            orderSummaryTextView.setText(sb.toString());
        }
    }
}
