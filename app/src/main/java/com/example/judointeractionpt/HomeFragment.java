package com.example.judointeractionpt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import com.example.judointeractionpt.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class HomeFragment extends Fragment {

    LinearLayout clubDetails_btn, judoBelts_btn, groupSessions_btn, judoHistory_btn, clubPhotos_btn, kidPregress_btn;

    DatabaseReference userRef;
    FirebaseUser firebase_user;

    DatabaseReference databaseReference;

    DatabaseReference databaseReference_forUsers;

    boolean verification_club; // 1 for club Timisoara and 0 for Zadareni
    boolean verification_user; // 1 for trainer and 0 for parent

    String store_judoClub;
    String store_TypeOfUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        clubDetails_btn = view.findViewById(R.id.clubDetails);
        judoBelts_btn = view.findViewById(R.id.judoBelts);
        groupSessions_btn = view.findViewById(R.id.GroupSessions);
        judoHistory_btn = view.findViewById(R.id.judoHistory);
        clubPhotos_btn = view.findViewById(R.id.clubPhotos);
        kidPregress_btn = view.findViewById(R.id.kidProgress);

        firebase_user = FirebaseAuth.getInstance().getCurrentUser();

        verifyClub(firebase_user);

        verifyUser(firebase_user);


        clubDetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClubDetails.class);
                startActivity(intent);
            }
        });

        kidPregress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KidProgress_parent.class);
                startActivity(intent);
            }
        });

        clubPhotos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClubPhotos.class);
                startActivity(intent);
            }
        });

        judoBelts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), JudoBelts.class);
                startActivity(intent);
            }
        });

        groupSessions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Objects.equals(verification_club, false))
                {
                    Intent intent = new Intent(getActivity(), GroupSessions.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), GroupSessions_Timisoara.class);
                    startActivity(intent);
                }

            }


        });

        kidPregress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Objects.equals(verification_user, true))
                {
                    Intent intent = new Intent(getActivity(), KidProgress_trainer.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), KidProgress_parent.class);
                    startActivity(intent);
                }

            }
        });

        judoHistory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), JudoHistory.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void verifyClub(FirebaseUser firebase_user) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user_obj = snapshot.getValue(User.class);

                store_judoClub = user_obj.getJudoclub();

                if (Objects.equals(store_judoClub, "Judo Club Timisoara")) {
                    verification_club = true;
                } else {
                    verification_club = false;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void verifyUser(FirebaseUser firebase_user) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user_obj = snapshot.getValue(User.class);

                store_TypeOfUser = user_obj.getEmail();

                if (Objects.equals(store_TypeOfUser, "siclovansebastian@yahoo.com")) {
                    verification_user = true;
                } else {
                    verification_user = false;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}

