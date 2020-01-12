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

import com.example.foodJournal.R;
import com.example.foodJournal.data.CONSTANT;
import com.example.foodJournal.data.InMemoryStorage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditPersonalProfileActivity extends AppCompatActivity {

    private TextView account;
    private EditText firstName;
    private EditText lastName;
    private EditText gender;
    private EditText age;
    private EditText weight;
    private EditText height;
    private TextView bmi;
    private EditText healthTarget;
    private TextView foodTimeLapse;
    private TextView waterTimeLapse;
    private String editUrl_complete;

    private int age_int;
    private Double weight_double;
    private Double height_double;
    private int bmi_result;
    private int bmr_result;
    int hydration_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_profile);

        setAllTextViews();

        Button confirmBtn = (Button) findViewById(R.id.confirmEditProfileBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_edit_health_condition();
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancelEditProfileBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPersonalProfileActivity.this, PersonalProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAllTextViews(){
        account = (TextView) findViewById(R.id.account_edit);
        firstName = (EditText) findViewById(R.id.firstName_edit);
        lastName = (EditText) findViewById(R.id.lastName_edit);
        gender = (EditText) findViewById(R.id.gender_edit);
        age = (EditText) findViewById(R.id.age_edit);
        weight = (EditText) findViewById(R.id.weight_edit);
        height = (EditText) findViewById(R.id.height_edit);
        bmi = (TextView) findViewById(R.id.bmi_edit);
        healthTarget = (EditText) findViewById(R.id.healthTarget_edit);
        foodTimeLapse = (TextView) findViewById(R.id.foodTimeLapse_edit);
        waterTimeLapse = (TextView) findViewById(R.id.waterTimeLapse_edit);


        account.setText(InMemoryStorage.getUsername());
        editUrl_complete = CONSTANT.editUrl_incomplete + InMemoryStorage.getUsername();

        if (InMemoryStorage.getFirstName() == null) {

        } else {
            firstName.setText(InMemoryStorage.getFirstName());
            lastName.setText(InMemoryStorage.getLastName());
            gender.setText(InMemoryStorage.getGender());
            age.setText(InMemoryStorage.getAge_str());
            weight.setText(InMemoryStorage.getWeight());
            height.setText(InMemoryStorage.getHeight());
            bmi.setText(InMemoryStorage.getBmi());
            healthTarget.setText(InMemoryStorage.getHealthTarget_str());
            foodTimeLapse.setText(InMemoryStorage.getFoodTimeLapse_str());
            waterTimeLapse.setText(InMemoryStorage.getWaterTimeLapse_str());
        }


    }

    private void do_edit_health_condition() {
        //Log.d("POST_EDIT_HEALTH_INFO", "POST Form Function Called.");
        //1.create client
        OkHttpClient client = new OkHttpClient();
        //Log.d("POST_EDIT_HEALTH_INFO", "Client Created.");

        //2.1.create request body
        if(!do_validate_edit()){
            return;
        }

        //bmi--kg/m^2: weight/(height^2)
        age_int = Integer.parseInt(age.getText().toString());
        weight_double = Double.valueOf(weight.getText().toString());
        height_double  = Double.valueOf(height.getText().toString());
        double temp = weight_double / (height_double * height_double/10000);
        bmi_result = (int) Math.round(temp);
        //bmr
        if (gender.getText().toString().equals("M")) {
            bmr_result = (int) Math.round((66 + 13.7 * weight_double + 5 * height_double - 6.8 * age_int));
        } else {
            bmr_result = (int) Math.round((655 + +9.6 * weight_double + 1.8 * height_double -4.7 * age_int));
        }
        //onces: if exercise is considered, 12 ounces every 30 minutes
        temp = weight_double * 2.2 * 2/3;
        hydration_result = (int) Math.round(temp);

        RequestBody body = new FormBody.Builder()
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!check what is the data type in the database !!!!!!!!!!!!!!
                .add("firstName", firstName.getText().toString())
                .add("lastName", lastName.getText().toString())
                .add("gender", gender.getText().toString())
                .add("age", age.getText().toString())
                .add("weight", weight.getText().toString())
                .add("height", height.getText().toString())
                .add("bmi",String.valueOf(bmi_result))
                .add("healthTarget",healthTarget.getText().toString())
                .add("foodTimeLapse", String.valueOf(bmr_result))
                .add("waterTimeLapse", String.valueOf(hydration_result))
                .build();

        //2.2.create request
        Request newRequest = new Request.Builder()
                .url(editUrl_complete)
                .post(body)
                .build();

        //Log.d("POST_EDIT_HEALTH_INFO", "Request Build Successful.");

        //3.send request and get response
        client.newCall(newRequest).enqueue(new Callback() {
            //3.1. on failure
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("POST_EDIT_HEALTH_INFO", e.getMessage());
                //e.printStackTrace();
            }

            //3.2. on success
            @Override
            public void onResponse(Call call, Response response) throws IOException {//4. handle the request
                if (response.isSuccessful()) {
                //    Log.d("POST_EDIT_HEALTH_INFO", "Got the Response.");
                    EditPersonalProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(EditPersonalProfileActivity.this, PersonalProfileActivity.class);

                            InMemoryStorage.setHealthInfo(firstName.getText().toString(),
                                    lastName.getText().toString(), gender.getText().toString(),age_int,
                                    weight.getText().toString(), height.getText().toString(), String.valueOf(bmi_result),
                                    Integer.parseInt(healthTarget.getText().toString()), bmr_result,hydration_result);

                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private  boolean do_validate_edit(){
        if(firstName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your First Name", Toast.LENGTH_LONG).show();
            return false;
        }
        if(lastName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your First Name", Toast.LENGTH_LONG).show();
            return false;
        }
        String gender_str = gender.getText().toString();
        if(!( gender_str.equals("M") || gender_str.equals("F") )){
            Toast.makeText(getApplicationContext(),"Please enter M/F to Gender", Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            Integer.parseInt(age.getText().toString());
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Please enter an integer to Age", Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            Integer.parseInt(healthTarget.getText().toString());
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Please enter an integer to Health Target", Toast.LENGTH_LONG).show();
            return false;
        }

        try{
            Float.parseFloat(weight.getText().toString());
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Please enter a decimal to Weight", Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            Float.parseFloat(height.getText().toString());
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Please enter a decimal to Height", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}

