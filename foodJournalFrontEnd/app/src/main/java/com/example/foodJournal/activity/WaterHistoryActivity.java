package com.example.foodJournal.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodJournal.R;
import com.example.foodJournal.adapter.WaterHistoryAdapter;
import com.example.foodJournal.database_model.WaterHistoryDBHelper;
import com.example.foodJournal.database_model.WaterHistorySchema;

import java.sql.Timestamp;
import java.util.ArrayList;

public class WaterHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private WaterHistoryAdapter mAdapter;

    private EditText mName;
    private EditText mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_history);

        //------------------database---------------------
        WaterHistoryDBHelper dbHelper = new WaterHistoryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        //---------------RecyclerView----------------------
        //--size
        //--layoutmanager
        //--adapter
        RecyclerView recyclerView = findViewById(R.id.rv_water_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String[]> dataList = getAllItems();//don't return null for ArrayList, that will throw null object exception
        mAdapter = new WaterHistoryAdapter(this, dataList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new WaterHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {

                String WHERE = WaterHistorySchema.WaterHistoryEntry.COLUMN_NAME + "= ? AND "
                        + WaterHistorySchema.WaterHistoryEntry.COLUMN_AMOUMT + "= ? AND "
                        + WaterHistorySchema.WaterHistoryEntry.COLUMN_TIMESTAMP + "= ? "  ;
                String[] WHEREARGS = new String[] {mAdapter.queryName(position),mAdapter.queryAmount(position),
                        mAdapter.queryTime(position)};
                mDatabase.delete(WaterHistorySchema.WaterHistoryEntry.TABLE_NAME, WHERE, WHEREARGS);
            }
        });
        //------------------other activity layout elements------------------
        mName = (EditText) findViewById(R.id.activity_water_history_name);
        mAmount = (EditText) findViewById(R.id.activity_water_history_amount);

        Button AddBtn = (Button) findViewById(R.id.activity_water_history_addBtn);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.activity_water_history_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterHistoryActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addItem() {

        String name = mName.getText().toString();
        String amount = mAmount.getText().toString();

        try {
            if (Integer.valueOf(amount) > 100000) {
                Toast.makeText(WaterHistoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(WaterHistoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.trim().length() == 0 || Integer.valueOf(amount) == 0 ) {
            return;
        }

        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis())) ;
        ContentValues cv = new ContentValues();//key-value pair
        cv.put(WaterHistorySchema.WaterHistoryEntry.COLUMN_NAME, name);
        cv.put(WaterHistorySchema.WaterHistoryEntry.COLUMN_AMOUMT, amount);
        cv.put(WaterHistorySchema.WaterHistoryEntry.COLUMN_TIMESTAMP, timestamp);
        mDatabase.insert(WaterHistorySchema.WaterHistoryEntry.TABLE_NAME, null, cv);
        mAdapter.addData(new String[]{name, amount, timestamp});
        mName.getText().clear();
        mAmount.getText().clear();
    }

    private ArrayList<String[]> getAllItems() {

        ArrayList<String[]> dataList = new ArrayList<String[]>(){};
        Cursor cursor;
        try{
             cursor = mDatabase.query(
                    WaterHistorySchema.WaterHistoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    WaterHistorySchema.WaterHistoryEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } catch(Exception e) {
            return dataList;
        }

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                String name = cursor.getString(
                        cursor.getColumnIndex(WaterHistorySchema.WaterHistoryEntry.COLUMN_NAME));
                int amount = cursor.getInt(
                        cursor.getColumnIndex(WaterHistorySchema.WaterHistoryEntry.COLUMN_AMOUMT));
                String time = cursor.getString(
                        cursor.getColumnIndex(WaterHistorySchema.WaterHistoryEntry.COLUMN_TIMESTAMP));
                dataList.add(new String[] {name, String.valueOf(amount), time});
                //Log.d("RECYCLE", name);
            }while(cursor.moveToNext());
        } else {
            Log.d("RECYCLE", "empty cursor");
        }

        return dataList;
    }
}

