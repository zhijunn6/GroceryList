package com.example.grocerylist;

import android.provider.BaseColumns;

public class GroceryContract {

    //Empty constructor
    //As only the string constants below are required
    //This is left empty as we are not going to use it in any way
    private GroceryContract() {}

    //Columns of the database table
    public static final class GroceryEntry implements BaseColumns{
        public static final String TABLE_NAME = "groceryList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
