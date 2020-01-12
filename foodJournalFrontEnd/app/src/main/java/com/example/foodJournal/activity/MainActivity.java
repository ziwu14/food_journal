package com.example.foodJournal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodJournal.data.CONSTANT;
import com.example.foodJournal.gson_model.HealthModel;
import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText passwd;
    private TextView info;
    private Button login;
    private Button signUp;
    private Button guest;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.account_edit);
        passwd = (EditText) findViewById(R.id.password);
        info = (TextView) findViewById(R.id.tv1);

        if (getIntent().hasExtra("account_from_signup")) {
            username.setText(getIntent().getExtras().getString("account_from_signup"));
            passwd.setText(getIntent().getExtras().getString("password_from_signup"));
        }

        info.setText("No of attempts remaining: 5");

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_retrieve_health_condition();
            }
        });

        signUp = (Button) findViewById(R.id.btnSign);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        guest = (Button) findViewById(R.id.btnGuest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuestActivity.class);
                startActivity(intent);
            }
        });

    }

    private void do_retrieve_health_condition() {
        //Log.d("POST_SIGNUP_FORM", "POST Form Function Called.");
        //1.create client
        OkHttpClient client = new OkHttpClient();
        //Log.d("POST_SIGNUP_FORM", "Client Created.");

        //2.1.create request body
        RequestBody body = new FormBody.Builder()
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!check what is the data type in the database !!!!!!!!!!!!!!
                .add("username", username.getText().toString())
                .add("password", passwd.getText().toString())
                .build();

        //2.2.create request
        Request newRequest = new Request.Builder()
                .url(CONSTANT.authUrl)
                .post(body)
                .build();

        //Log.d("POST_AUTH", "Request Build Successful.");

        //3.send request and get response
        client.newCall(newRequest).enqueue(new Callback() {
            //3.1. on failure
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("POST_AUTH", e.getMessage());
                //e.printStackTrace();
            }

            //3.2. on success
            @Override
            public void onResponse(Call call, Response response) throws IOException {//4. handle the request
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    //Log.d("POST_AUTH", "Got the Response");

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!myResponse.equals("User not exists")) {
                                Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                                InMemoryStorage.setUsername(username.getText().toString());
                                //remove the square bracket, since we have only one object in the QuerySet
                                String json= myResponse.replaceAll("\\[|\\]","");

                                if(!json.equals("")){
                                    Gson gson = new Gson();
                                    HealthModel healthInfo = gson.fromJson(json, HealthModel.class);
                                 //   Log.d("PERSONAL_PROFILE","receved health_info");
                                 //   Log.d("PERSONAL_PROFILE",json);

                                    InMemoryStorage.setHealthInfo(healthInfo.getFirstName(),healthInfo.getLastName(),
                                            healthInfo.getGender(),healthInfo.getAge(),healthInfo.getWeight(),healthInfo.getHeight(),
                                            healthInfo.getBMI(),healthInfo.getHealthTarget(),healthInfo.getFoodTimeLapse(),healthInfo.getWaterTimeLapse());

                                } else {
                                   // Log.d("PERSONAL_PROFILE","No health info for this account");
                                }

                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(MainActivity.this, "Invalid user or passwd", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
