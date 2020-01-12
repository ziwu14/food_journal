package com.example.foodJournal.activity;

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

import com.example.foodJournal.R;
import com.example.foodJournal.adapter.FoodHistoryAdapter;
import com.example.foodJournal.database_model.FoodHistoryDBHelper;
import com.example.foodJournal.database_model.FoodHistorySchema;

import java.util.ArrayList;

public class FoodHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private FoodHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_history);

        //------------------database---------------------
        FoodHistoryDBHelper dbHelper = new FoodHistoryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        //---------------RecyclerView----------------------
        //--size
        //--layoutmanager
        //--adapter
        RecyclerView recyclerView = findViewById(R.id.rv_food_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String[]> dataList = getAllItems();//don't return null for ArrayList, that will throw null object exception
        for (int i =0 ; i < dataList.size() ; i++) {
            String entry  =  (dataList.get(i))[0] + (dataList.get(i))[1] + (dataList.get(i))[2] + (dataList.get(i))[3];
            Log.d("FOODHISTORY", entry);
        }
        mAdapter = new FoodHistoryAdapter(FoodHistoryActivity.this, dataList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FoodHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {

                String WHERE = FoodHistorySchema.FoodHistoryEntry.COLUMN_NAME + "= ? AND " +
                        FoodHistorySchema.FoodHistoryEntry.COLUMN_AMOUMT + "= ? AND " +
                        FoodHistorySchema.FoodHistoryEntry.COLUMN_CALORIE + "= ? AND " +
                        FoodHistorySchema.FoodHistoryEntry.COLUMN_TIMESTAMP + "= ? ";
                String[] WHEREARGS = new String[] {mAdapter.queryName(position), mAdapter.queryAmount(position),
                        mAdapter.queryCalorie(position),mAdapter.queryTime(position)};
                mDatabase.delete(FoodHistorySchema.FoodHistoryEntry.TABLE_NAME, WHERE, WHEREARGS);
            }
        });

        Button backBtn = (Button) findViewById(R.id.activity_food_history_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodHistoryActivity.this, FoodManagementActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<String[]> getAllItems() {

        ArrayList<String[]> dataList = new ArrayList<String[]>(){};
        Cursor cursor;
        try {
            cursor = mDatabase.query(
                    FoodHistorySchema.FoodHistoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    FoodHistorySchema.FoodHistoryEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } catch (Exception e) {
            return dataList;
        }

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                String name = cursor.getString(
                        cursor.getColumnIndex(FoodHistorySchema.FoodHistoryEntry.COLUMN_NAME));
                float amount = cursor.getFloat(
                        cursor.getColumnIndex(FoodHistorySchema.FoodHistoryEntry.COLUMN_AMOUMT));
                float caloire = cursor.getFloat(
                        cursor.getColumnIndex(FoodHistorySchema.FoodHistoryEntry.COLUMN_CALORIE));
                String time = cursor.getString(
                        cursor.getColumnIndex(FoodHistorySchema.FoodHistoryEntry.COLUMN_TIMESTAMP));
                dataList.add(new String[] {name, String.valueOf(amount), String.valueOf(caloire),time});
                //Log.d("RECYCLE", name);
            }while(cursor.moveToNext());
        } else {
            Log.d("RECYCLE", "empty cursor");
        }

        return dataList;
    }
}
