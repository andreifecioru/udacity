package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
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
    @BindString(R.string.submit_your_order) String submitYourOrderText;
    @BindString(R.string.who_you_are) String whoYouAreText;
    @BindString(R.string.order_coffee_first) String orderCoffeeFirstText;
    @BindString(R.string.greeting) String helloText;
    @BindString(R.string.you_have_ordered) String youHaveOrderText;
    @BindString(R.string.coffees) String coffeesText;
    @BindString(R.string.vanilla_topping) String vanillaToppingText;
    @BindString(R.string.milk_topping) String milkToppingText;
    @BindString(R.string.total_price) String totalPriceText;
    @BindString(R.string.thank_you) String thankYouText;

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
        if (quantity == 0) {
            Toast.makeText(this, orderCoffeeFirstText, Toast.LENGTH_SHORT).show();
            return;
        }

        if ("".equals(nameEditText.getText().toString())) {
            Toast.makeText(this, whoYouAreText, Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("mailto:office@justjava.com")
                .buildUpon()
                .appendQueryParameter("subject", getString(R.string.mail_subject, calculatePrice()))
                .appendQueryParameter("body", composeOrderSummaryText())
                .build();

        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            startActivity(Intent.createChooser(intent, submitYourOrderText));
        }
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

    private String composeOrderSummaryText() {
        String retVal;

        if (quantity == 0) {
            retVal = orderCoffeeFirstText;
        } else {
            String name = nameEditText.getText().toString();
            if ("".equals(name)) {
                retVal = whoYouAreText;
            } else {
                StringBuilder sb = new StringBuilder("");
                sb.append(getString(R.string.greeting, name)).append("\n");
                sb.append(youHaveOrderText).append("\n");
                sb.append("  - ").append(getString(R.string.coffees, quantity)).append("\n");
                if (vanillaToppingCheckBox.isChecked())
                    sb.append("  - ").append(vanillaToppingText).append("\n");
                if (milkToppingCheckBox.isChecked())
                    sb.append("  - ").append(milkToppingText).append("\n");
                sb.append(getString(R.string.total_price, NumberFormat.getCurrencyInstance().format(calculatePrice()))).append("\n");
                sb.append(thankYouText);
                retVal = sb.toString();
            }
        }

        return retVal;
    }

    private void displayOrderSummary() {
        orderSummaryTextView.setText(composeOrderSummaryText());
    }
}
