package com.example.judointeractionpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.judointeractionpt.All_JudoBelts_classes.Blue_belt;
import com.example.judointeractionpt.All_JudoBelts_classes.Brown_belt;
import com.example.judointeractionpt.All_JudoBelts_classes.Green_belt;
import com.example.judointeractionpt.All_JudoBelts_classes.Orange_belt;
import com.example.judointeractionpt.All_JudoBelts_classes.White_belt;
import com.example.judointeractionpt.All_JudoBelts_classes.Yellow_belt;


public class JudoBelts extends AppCompatActivity {

    LinearLayout whiteBelt_btn, yellowBelt_btn, orangeBelt_btn, greenBelt_btn, blueBelt_btn, brownBelt_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judo_belts);

        whiteBelt_btn = findViewById(R.id.White_belt);
        yellowBelt_btn = findViewById(R.id.Yellow_belt);
        orangeBelt_btn = findViewById(R.id.Orange_belt);
        greenBelt_btn = findViewById(R.id.Green_belt);
        blueBelt_btn = findViewById(R.id.Blue_belt);
        brownBelt_btn = findViewById(R.id.Brown_belt);


        whiteBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, White_belt.class);
                startActivity(intent);
            }
        });

        yellowBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, Yellow_belt.class);
                startActivity(intent);
            }
        });

        orangeBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, Orange_belt.class);
                startActivity(intent);
            }
        });

        greenBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, Green_belt.class);
                startActivity(intent);
            }
        });

        blueBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, Blue_belt.class);
                startActivity(intent);
            }
        });

        brownBelt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JudoBelts.this, Brown_belt.class);
                startActivity(intent);
            }
        });





    }
}