package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.judointeractionpt.Model.Kids;
import com.example.judointeractionpt.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class KidProgress_parent extends AppCompatActivity {

    TextView name, code, belt_type, current_week, number_presentWeek, number_sessions, qualification;

    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    Button send_code;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_progress_parent);


        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.kidName_parent);
        code = findViewById(R.id.kidCode_parent);
        belt_type = findViewById(R.id.kidBelt_parent);
        current_week = findViewById(R.id.kidCurrentWeek_parent);
        number_presentWeek = findViewById(R.id.kidPresentWeek_parent);
        number_sessions = findViewById(R.id.kidSessions_parent);
        qualification = findViewById(R.id.kidQualification_parent);

        send_code = findViewById(R.id.kidProgress_sendCode);

        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String store_code = code.getText().toString().trim();

                if (TextUtils.isEmpty(store_code))
                {
                    code.setError("Please complete Child's unique code to see information about your child");
                    return;
                }
                else
                {
                    display_info_aboutKid(store_code);
                }


            }
        });

    }

    private void display_info_aboutKid(String store_code) {

        FirebaseDatabase.getInstance().getReference().child("Kids").child(store_code).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kids kids = snapshot.getValue(Kids.class);

                name.setVisibility(View.VISIBLE);
                belt_type.setVisibility(View.VISIBLE);
                current_week.setVisibility(View.VISIBLE);
                number_presentWeek.setVisibility(View.VISIBLE);
                number_sessions.setVisibility(View.VISIBLE);
                qualification.setVisibility(View.VISIBLE);

                send_code.setVisibility(View.GONE);
                code.setVisibility(View.GONE);

                name.setText("Name: " + kids.getName());
                belt_type.setText("Belt type: "+ kids.getBelt_type());
                current_week.setText("Current week: " + kids.getCurrent_week());
                number_presentWeek.setText("Number present this week: "+ kids.getNumber_presentWeek());
                number_sessions.setText("number of training sessions: " +kids.getNumber_sessions());
                qualification.setText("Qualification: " +kids.getQualification());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}