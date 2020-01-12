package com.example.foodJournal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodJournal.R;
import com.example.foodJournal.data.InMemoryStorage;


public class PersonalProfileActivity extends AppCompatActivity {

    private TextView account;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private TextView age;
    private TextView weight;
    private TextView height;
    private TextView bmi;
    private TextView healthTarget;
    private TextView foodTimeLapse;
    private  TextView waterTimeLapse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("PERSONAL_PROFILE","enter the personal activity");
        setContentView(R.layout.activity_personal_profile);

        setAllTextViews();

        Button editBtn  = (Button) findViewById(R.id.editPersonalProfileBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPersonalProfileActivity.class);
                startActivity(intent);
            }
        });

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                InMemoryStorage.clearAll();
                startActivity(intent);
            }
        });

        Button dangerFoodBtn = findViewById(R.id.danger_foodBtn);
        dangerFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangerFoodActivity.class);
                startActivity(intent);
            }
        });

        ImageButton backBtn = findViewById(R.id.activity_personal_profile_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);
            }
        });

    }
    private void setAllTextViews(){
        account = (TextView) findViewById(R.id.account_edit);
        firstName = (TextView) findViewById(R.id.firstName_edit);
        lastName = (TextView) findViewById(R.id.lastName);
        gender = (TextView) findViewById(R.id.gender);
        age = (TextView) findViewById(R.id.age);
        weight = (TextView) findViewById(R.id.weight);
        height = (TextView) findViewById(R.id.height);
        bmi = (TextView) findViewById(R.id.bmi);
        healthTarget = (TextView) findViewById(R.id.healthTarget);
        foodTimeLapse = (TextView) findViewById(R.id.foodTimeLapse);
        waterTimeLapse = (TextView) findViewById(R.id.waterTimeLapse);

        if (InMemoryStorage.getFirstName() == null) {

        } else {
            account.setText(InMemoryStorage.getUsername());
            firstName.setText(" " + InMemoryStorage.getFirstName());
            lastName.setText(" " + InMemoryStorage.getLastName());
            gender.setText(InMemoryStorage.getGender());
            age.setText(InMemoryStorage.getAge().toString());
            weight.setText(InMemoryStorage.getWeight());
            height.setText(InMemoryStorage.getHeight());
            bmi.setText(InMemoryStorage.getBmi());
            healthTarget.setText(InMemoryStorage.getHealthTarget_str());
            foodTimeLapse.setText(InMemoryStorage.getFoodTimeLapse_str());
            waterTimeLapse.setText(InMemoryStorage.getWaterTimeLapse_str());
        }

    }

}
