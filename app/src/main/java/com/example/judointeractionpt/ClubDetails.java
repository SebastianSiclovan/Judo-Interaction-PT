package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.judointeractionpt.Model.User;
import com.example.judointeractionpt.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ClubDetails extends AppCompatActivity {

    ImageView backBtn;
    TextView clubName, trainerName, locationAdress, contactPhone, clubMembers;
    TextView scheduleMonday, scheduleTuesday, scheduleWednesday, scheduleThursday, scheduleFriday, scheduleSaturday, scheduleSunday;

    FirebaseUser user;
    DatabaseReference userRef;

    String judoClub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_club_details);

        clubName = findViewById(R.id.name_var);
        trainerName = findViewById(R.id.trainer_var);
        locationAdress = findViewById(R.id.location_var);
        contactPhone = findViewById(R.id.contact_var);
        clubMembers = findViewById(R.id.members_var);

        scheduleMonday = findViewById(R.id.monday_var);
        scheduleTuesday = findViewById(R.id.tuesday_var);
        scheduleWednesday = findViewById(R.id.wednesday_var);
        scheduleThursday = findViewById(R.id.thursday_var);
        scheduleFriday = findViewById(R.id.friday_var);
        scheduleSaturday = findViewById(R.id.saturday_var);
        scheduleSunday = findViewById(R.id.sunday_var);

        backBtn = findViewById(R.id.ClubDetails_back);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        userInformation();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void userInformation() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                judoClub = user.getJudoclub();

                if (Objects.equals(judoClub, "Judo Club Zadareni"))
                {
                    clubName.setText(judoClub);
                    trainerName.setText("Andrei loghin");
                    locationAdress.setText("Zadareni, str principala");
                    contactPhone.setText("+40 xxxxxxxx");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}