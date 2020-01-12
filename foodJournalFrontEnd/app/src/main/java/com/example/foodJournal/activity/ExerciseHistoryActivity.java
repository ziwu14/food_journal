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
import com.example.foodJournal.adapter.ExerciseHistoryAdapter;
import com.example.foodJournal.database_model.ExerciseHistoryDBHelper;
import com.example.foodJournal.database_model.ExerciseHistorySchema;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ExerciseHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private ExerciseHistoryAdapter mAdapter;

    private EditText mName;
    private EditText mAmount;
    private EditText mCalorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);

        //------------------database---------------------
        ExerciseHistoryDBHelper dbHelper = new ExerciseHistoryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        //---------------RecyclerView----------------------
        //--size
        //--layoutmanager
        //--adapter
        RecyclerView recyclerView = findViewById(R.id.rv_exercise_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String[]> dataList = getAllItems();//don't return null for ArrayList, that will throw null object exception
        mAdapter = new ExerciseHistoryAdapter(this, dataList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExerciseHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {

                String WHERE = ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_NAME + "= ? AND "
                        + ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_AMOUMT + "=? AND "
                        + ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_CALORIE + "=? AND "
                        + ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_TIMESTAMP + "=? ";
                String[] WHEREARGS = new String[]{mAdapter.queryName(position),mAdapter.queryAmount(position),
                        mAdapter.queryCalorie(position),mAdapter.queryTime(position)};
                mDatabase.delete(ExerciseHistorySchema.ExerciseHistoryEntry.TABLE_NAME, WHERE, WHEREARGS    );
            }
        });
        //------------------other activity layout elements------------------
        mName = (EditText) findViewById(R.id.activity_exercise_history_name);
        mAmount = (EditText) findViewById(R.id.activity_exercise_history_amount);
        mCalorie = (EditText) findViewById(R.id.activity_exercise_history_calorie);

        Button AddBtn = (Button) findViewById(R.id.activity_exercise_history_addBtn);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.activity_exercise_history_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ExerciseHistoryActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addItem() {

        String name = mName.getText().toString();
        String amount = mAmount.getText().toString();
        String calorie = mCalorie.getText().toString();
        try {
            if (name.trim().length() == 0 || Integer.valueOf(amount) == 0 || Integer.valueOf(calorie) == 0 || Integer.valueOf(calorie) > 10000) {
                Toast.makeText(ExerciseHistoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();

                return;
            }
        } catch (Exception e) {
            Toast.makeText(ExerciseHistoryActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();

            return;
        }



        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis())) ;
        ContentValues cv = new ContentValues();//key-value pair
        cv.put(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_NAME, name);
        cv.put(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_AMOUMT, amount);
        cv.put(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_CALORIE, calorie);
        cv.put(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_TIMESTAMP, timestamp);
        mDatabase.insert(ExerciseHistorySchema.ExerciseHistoryEntry.TABLE_NAME, null, cv);
        mAdapter.addData(new String[]{name, amount, calorie, timestamp});
        mName.getText().clear();
        mAmount.getText().clear();
        mCalorie.getText().clear();
    }

    private ArrayList<String[]> getAllItems() {

        ArrayList<String[]> dataList = new ArrayList<String[]>(){};
        Cursor cursor;
        try {
            cursor = mDatabase.query(
                    ExerciseHistorySchema.ExerciseHistoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } catch (Exception e) {
            return dataList;
        }

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                String name = cursor.getString(
                        cursor.getColumnIndex(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_NAME));
                int amount = cursor.getInt(
                        cursor.getColumnIndex(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_AMOUMT));
                int calorie = cursor.getInt(
                        cursor.getColumnIndex(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_CALORIE));
                String time = cursor.getString(
                        cursor.getColumnIndex(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_TIMESTAMP));
                dataList.add(new String[] {name, String.valueOf(amount),String.valueOf(calorie), time});
                //Log.d("RECYCLE", name);
            }while(cursor.moveToNext());
        } else {
            Log.d("RECYCLE", "empty cursor");
        }

        return dataList;
    }
}

