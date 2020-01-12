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
import com.example.foodJournal.R;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    //#activity info
    private EditText username;
    private EditText password;
    private EditText email;

    private Button getBtn;
    private TextView resultTextView;
    private Button postFormBtn;
    private Button cancelBtn;
    private Button passwdPromptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("OKHTTP3", "Back to start.");
        super.onCreate(savedInstanceState);
        //Log.d("SIGNUPACTIVITY","enter the activity");
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.account_edit);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);

        postFormBtn = (Button) findViewById(R.id.postAccountBtn);
        postFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_post_form_request();
            }
        });


        cancelBtn = (Button) findViewById(R.id.cancelAccountBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        passwdPromptBtn = (Button) findViewById(R.id.promptAccountBtn);
        passwdPromptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promptText = "Password must contain minimum 8 maximum 20 characters at least 1 Alphabet, 1 Number and 1 Special Character";
                Toast.makeText(getApplicationContext(),promptText, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void do_post_form_request() {
        //Log.d("POST_SIGNUP_FORM", "POST Form Function Called.");
        //1.create client
        OkHttpClient client = new OkHttpClient();
        //Log.d("POST_SIGNUP_FORM", "Client Created.");

        //2.1.create request body
        if(do_validate_username(username.getText().toString())){
            Toast.makeText(getApplicationContext(), "invalid username, over 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(do_validate_email(email.getText().toString()) == false){
            Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(do_validate_passwd(password.getText().toString()) == false){
            Toast.makeText(getApplicationContext(), "invalid password", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody body = new FormBody.Builder()
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!check what is the data type in the database !!!!!!!!!!!!!!
                .add("username", username.getText().toString())
                .add("password", password.getText().toString())
                .add("email", email.getText().toString())
                .build();

        //2.2.create request
        Request newRequest = new Request.Builder()
                .url(CONSTANT.signupUrl)
                .post(body)
                .build();

        //Log.d("POST_SIGNUP_FORM", "Request Build Successful.");

        //3.send request and get response
        client.newCall(newRequest).enqueue(new Callback() {
            //3.1. on failure
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("POST_SIGNUP_FORM", e.getMessage());
                //e.printStackTrace();
            }

            //3.2. on success
            @Override
            public void onResponse(Call call, Response response) throws IOException {//4. handle the request
                if (response.isSuccessful()) {
                //    Log.d("POST_SIGNUP_FORM", "Got the Response.");
                    final String myResponse = response.body().string();
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!myResponse.equals("User exists")){
                                //Log.d("POST_SIGNUP_FORM",myResponse);
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("account_from_signup",username.getText().toString());
                                intent.putExtra("password_from_signup",password.getText().toString());
                                startActivity(intent);
                            } else{
                             //   Log.d("POST_SIGNUP_FORM","User exists");
                                Toast.makeText(getApplicationContext(), "User exists", Toast.LENGTH_SHORT).show();
                            }
                    }
                    });
                }
            }
        });

    }


    private boolean do_validate_username(final String username){
        if(username.length() > 20){
            return true;
        }
        return false;
    }
    private boolean do_validate_passwd(final String password) {

        if(password.length() > 20){
            return false;
        }

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    private boolean do_validate_email(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
