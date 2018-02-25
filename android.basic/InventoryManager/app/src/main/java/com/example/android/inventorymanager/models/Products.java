package com.example.android.inventorymanager.models;


import java.util.ArrayList;
import java.util.List;

final public class Products {
    static public List<Product> generateDummyProducts() {
        List<Product> result = new ArrayList<>();

        result.add(new Product("Wooden chair", 3, "The Carpenter's Shop", "800-088-22345"));
        result.add(new Product("Wooden table", 5, "The Carpenter's Shop", "800-088-22345"));
        result.add(new Product("Metal bench", 2, "The Iron Wizards", "800-900-12345"));
        result.add(new Product("Metal flower stand", 8, "The Iron Wizards", "800-900-12345"));

        return result;
    }
}
