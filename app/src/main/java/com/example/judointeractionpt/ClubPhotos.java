package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.judointeractionpt.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ClubPhotos extends AppCompatActivity {

    String store_judoClub;

    TextView available_pictures, notAvailable_pictures;
    ImageView zadareni_picture, timisoara_picture;

    FirebaseUser firebase_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_photos);

        available_pictures = findViewById(R.id.clubPhotos_availablePhotos);
        notAvailable_pictures = findViewById(R.id.clubPhotos_notAvailablePhotos);

        zadareni_picture = findViewById(R.id.image_ZadareniClub);
        timisoara_picture = findViewById(R.id.image_TimisoaraClub);

        firebase_user = FirebaseAuth.getInstance().getCurrentUser();

        display_pictures(firebase_user);

    }


    private void display_pictures(FirebaseUser firebase_user) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user_obj = snapshot.getValue(User.class);

                store_judoClub = user_obj.getJudoclub();

                if (Objects.equals(store_judoClub, "Judo Club Timisoara")) {
                    notAvailable_pictures.setVisibility(View.VISIBLE);
                    timisoara_picture.setVisibility(View.VISIBLE);
                } else {
                    available_pictures.setVisibility(View.VISIBLE);
                    zadareni_picture.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}