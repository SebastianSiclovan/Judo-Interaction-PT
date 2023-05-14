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
    TextView clubName, trainerName, locationAdress, contactPhone;
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

                // variables to show info for Judo Club Zadareni
                String TrainerName_forZadareni = "Andrei loghin";
                String locationAdress_forZadareni = "Zadareni, str principala";
                String contactPhone_forZadareni = "+40 xxxxxxxx";
                String [] hoursWeek_forZadareni = new String[] {"08:00-16:00", "09:00-16:00", "10:00-16:00", "09:00-16:00", "11:00-16:00", "13:00-16:00", "closed"};

                // variables to show info for Judo Club Timisoara
                String TrainerName_forTimisoara = "Raul Xipas";
                String locationAdress_forTimisoara = "Timisoara, Bd-ul Eroilor Tisa, nr. 11";
                String contactPhone_forTimisoara = "+50 xxxxxxxx";
                String [] hoursWeek_forTimisoara = new String[] {"08:30-16:30", "09:30-16:30", "10:30-16:30", "09:30-16:30", "11:30-16:00", "14:00-17:00", "closed"};


                TextView[] schedules = new TextView[] {scheduleMonday, scheduleTuesday, scheduleWednesday, scheduleThursday, scheduleFriday, scheduleSaturday, scheduleSunday};
                User user = snapshot.getValue(User.class);





                judoClub = user.getJudoclub();

                if (Objects.equals(judoClub, "Judo Club Zadareni"))
                {
                    clubName.setText(judoClub);
                    trainerName.setText(TrainerName_forZadareni);
                    locationAdress.setText(locationAdress_forZadareni);
                    contactPhone.setText(contactPhone_forZadareni);

                    for (int i = 0; i < hoursWeek_forZadareni.length; i++)
                    {
                        schedules[i].setText(hoursWeek_forZadareni[i]);
                    }

                }
                else if (Objects.equals(judoClub, "Judo Club Timisoara"))
                {
                    clubName.setText(judoClub);
                    trainerName.setText(TrainerName_forTimisoara);
                    locationAdress.setText(locationAdress_forTimisoara);
                    contactPhone.setText(contactPhone_forTimisoara);

                    for (int i = 0; i < hoursWeek_forZadareni.length; i++)
                    {
                        schedules[i].setText(hoursWeek_forTimisoara[i]);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}