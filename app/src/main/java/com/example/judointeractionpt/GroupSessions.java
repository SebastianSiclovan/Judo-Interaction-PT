package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.judointeractionpt.Model.User;
import com.google.android.material.badge.BadgeUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GroupSessions extends AppCompatActivity {

    CalendarView calendarView;
    EditText addEvent;
    TextView selectDate;
    TextView showEvent;

    Button addEvent_btn;
    Button deleteEvent_btn;



    String dateSelected_selectDataText;
    String dataSelected_realTimeDatabase;

    String store_addEvent;
    String store_showEvent;
    String store_userEmail;

    DatabaseReference databaseReference;

    DatabaseReference databaseReference_forUsers;

    FirebaseUser firebase_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sessions);

        calendarView = findViewById(R.id.calendarView);
        addEvent = findViewById(R.id.GroupSessions_addEvent);
        selectDate = findViewById(R.id.GroupSessions_selectDate);

        addEvent_btn = findViewById(R.id.GroupSessions_addEvent_btn);
        deleteEvent_btn = findViewById(R.id.GroupSessions_deleteEvent_btn);

        showEvent = findViewById(R.id.GroupSessions_showEvent);


        firebase_user = FirebaseAuth.getInstance().getCurrentUser();

        verifyUser(firebase_user);

        databaseReference = FirebaseDatabase.getInstance().getReference("calendar events");

        addEvent.setVisibility(View.GONE);






        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                 dateSelected_selectDataText = Integer.toString(date) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);

                 dataSelected_realTimeDatabase = Integer.toString(date) +  Integer.toString(month+1) + Integer.toString(year);
                 selectDate.setText("selected date: " + dateSelected_selectDataText);
                 calendarClicked();
            }
        });

        deleteEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Objects.equals(store_showEvent, "-> No training session"))
                {
                    Toast.makeText(GroupSessions.this, "No training is planned at this time", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (dateSelected_selectDataText == null)
                {
                    Toast.makeText(GroupSessions.this, "Please select a date to delete a training", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    databaseReference.child(dataSelected_realTimeDatabase).removeValue();
                    Toast.makeText(GroupSessions.this, "Now you delete a training for " + dateSelected_selectDataText, Toast.LENGTH_SHORT).show();
                }





            }
        });

        addEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store_addEvent = addEvent.getText().toString();

               String regex = "\\d{2}:\\d{2}-\\d{2}:\\d{2}\\s*\\|\\s*training";

                addEvent.setVisibility(View.VISIBLE);

                if (dateSelected_selectDataText == null)
                {
                    Toast.makeText(GroupSessions.this, "Please select a date to set a training", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (store_addEvent.isEmpty())
                {
                    Toast.makeText(GroupSessions.this, "You didn't enter a training", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!store_addEvent.matches(regex))
                {
                    addEvent.setError("The text entered must be in the form \"HH:mm-HH:mm | training\" (E.g 12:00-14:00 | training)");
                }
                else if (store_showEvent != "-> No training session")
                {
                    Toast.makeText(GroupSessions.this, "There is already a training on that date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    databaseReference.child(dataSelected_realTimeDatabase).setValue(addEvent.getText().toString());
                    Toast.makeText(GroupSessions.this, "Now you set a training for " + dateSelected_selectDataText, Toast.LENGTH_SHORT).show();
                }






            }
        });

    }

    // verification if the user is a trainer or not.
    private void verifyUser(FirebaseUser firebase_user) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user_obj = snapshot.getValue(User.class);

                store_userEmail = user_obj.getEmail();

                if (!Objects.equals(store_userEmail, "siclovansebastian@yahoo.com")) {
                    addEvent.setVisibility(View.GONE);
                    addEvent_btn.setVisibility(View.GONE);
                    deleteEvent_btn.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void calendarClicked()
    {
        databaseReference.child(dataSelected_realTimeDatabase).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null)
                {
                    store_showEvent = "-> " + snapshot.getValue().toString();
                    showEvent.setText(store_showEvent);

                }
                else
                {
                    store_showEvent = "-> No training session";
                    showEvent.setText(store_showEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}