package com.debkbanerji.mapingo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcw0805 on 3/25/17.
 */

public class Order {

    private double totalPrice;
    private int orderNum;
    private List<String> items;

    public Order(List<String> items, int orderNum) {
        this.items = items;
        this.orderNum = orderNum;

        for (String i : items) {
            String[] splitItem = i.split(" \\$");
            double price = Double.parseDouble(splitItem[splitItem.length - 1]);
            totalPrice += price;
        }
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
//        String priceString = currencyFormatter.format(totalPrice);
//
//        totalPrice = Double.parseDouble(priceString.split("\\$")[0]);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getOrderNum() {
        return orderNum;
    }

    public List<String> getItems() {
        return items;
    }
}
