package com.example.grocerylist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase sqLiteDatabase;
    private GroceryAdapter groceryAdapter;
    private EditText itemInput;
    private TextView itemNo;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this, GroceryContract.GroceryEntry.TABLE_NAME, null, 1);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groceryAdapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(groceryAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        itemInput = findViewById(R.id.item_et);
        itemNo = findViewById(R.id.itemNo_txt);

        Button increaseBtn = findViewById(R.id.increase_btn);
        Button decreaseBtn = findViewById(R.id.decrease_btn);
        Button addBtn = findViewById(R.id.add_btn);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void increase() {
        count++;
        itemNo.setText(String.valueOf(count));
    }

    private void decrease() {
        if (count > 0) {
            count--;
            itemNo.setText(String.valueOf(count));
        }
    }

    private void addItem() {
        if (itemInput.getText().toString().trim().length() == 0 || count == 0){
            return;
        }

        String itemName = itemInput.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroceryContract.GroceryEntry.COLUMN_NAME, itemName);
        contentValues.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, count);

        sqLiteDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, contentValues);
        groceryAdapter.swapCursor(getAllItems());

        itemInput.getText().clear();
    }

    private void removeItem(long id) {
        sqLiteDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID + "=" + id, null);

        groceryAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return sqLiteDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
