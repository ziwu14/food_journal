package com.example.foodJournal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.foodJournal.R;
import com.example.foodJournal.adapter.DangerFoodAdapter;
import com.example.foodJournal.data.CONSTANT;
import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.gson_model.DangerFoodModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DangerFoodActivity extends AppCompatActivity {

    private DangerFoodAdapter mAdapter;
    private RecyclerView recyclerView;
    private final String getDangerFoodUrl_complete = CONSTANT.getDangerFoodUrl_incomplete + InMemoryStorage.getUsername();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_food);

        //---------------RecyclerView----------------------
        //--size
        //--layoutmanager
        //--adapter
        recyclerView = findViewById(R.id.rv_danger_food);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DangerFoodAdapter(this, new ArrayList<String[]>(){});
        recyclerView.setAdapter(mAdapter);
        do_receive_from_server();
        mAdapter.setOnItemClickListener(new DangerFoodAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {
                do_remove_from_server(mAdapter.queryName(position));
            }
        });
        //------------------other activity layout elements------------------

//        Button AddBtn = (Button) findViewById(R.id.activity_danger_food_addBtn);
//        AddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addItem();
//            }
//        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.activity_danger_food_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(DangerFoodActivity.this, PersonalProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * network access must be on non-main/non-UI thread
     * but we need to merge the info to UI thread
     * so after receiving response, we must runOnUiThread
     * NB: the notifyDataChanged method is inside the adapter, so we define a swap method
     */

    private void do_receive_from_server() {
        OkHttpClient client = new OkHttpClient();
        Request newRequest = new Request.Builder()
                .url(getDangerFoodUrl_complete)
                .build();

        client.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RECEIVE DANGER FOOD", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json_string = response.body().string();
                    DangerFoodActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String[]> dataList = getAllItems(json_string);
                            mAdapter.swapDataList(dataList);
                        }
                    });
                }
            }
        });
    }

    public ArrayList<String[]> getAllItems(String json) {

        ArrayList<String[]> dataList = new ArrayList<String[]>(){};
        if (json != null){
            //https://www.youtube.com/watch?v=ZZddxpxGQPE&list=PLpUMhvC6l7AOy4UEORSutzFus98n-Es_l&index=3
            Gson gson = new Gson();
            DangerFoodModel[] foodList = gson.fromJson(json, DangerFoodModel[].class);
            for (int i = 0; i < foodList.length; i ++) {
                String[] temp = new String[] {foodList[i].getName()};
                dataList.add(i, temp);
            }
        }
        return dataList;
    }

//    private void addItem() {
//
//        String name = mName.getText().toString();
//        if (name.trim().length() == 0) {
//            return;
//        }
//
//        do_upload_to_server(name);
//
//        mAdapter.addData(new String[]{name});
//        mName.getText().clear();
//    }

//    private void do_upload_to_server(String foodName) {
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!check what is the data type in the database !!!!!!!!!!!!!!
//                .add("name", foodName)
//                .build();
//
//        Request newRequest = new Request.Builder()
//                .url(getDangerFoodUrl_complete)
//                .post(body)
//                .build();
//
//        client.newCall(newRequest).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("UPLOAD DANGER FOOD", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    Log.d("UPLOAD DANGER FOOD", "onResponse: ");
//                }
//            }
//        });
//    }

    private void do_remove_from_server(String foodName) {
        OkHttpClient client = new OkHttpClient();
        final String deleteDangerFoodUrl_complete = getDangerFoodUrl_complete + "&name=" + foodName;

        Request newRequest = new Request.Builder()
                .url(deleteDangerFoodUrl_complete)
                .build();

        client.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("DELETE DANGER FOOD", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("DELETE DANGER FOOD", "onResponse: ");
                }
            }
        });
    }
}

