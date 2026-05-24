package com.redisjava.grocery;

public record GroceryItem(
        int id, 
        String name, 
        String category, 
        String unit, 
        boolean organic
) {
}
