package com.example.judointeractionpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button login_btn;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        login_btn = findViewById(R.id.firstActivity_Login);
        register_btn = findViewById(R.id.firstActivity_Register);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting activity to login.
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting activity to Register
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }
}