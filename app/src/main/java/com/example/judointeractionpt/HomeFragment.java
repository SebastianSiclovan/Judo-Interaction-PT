package com.example.judointeractionpt;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;


public class HomeFragment extends Fragment {

    LinearLayout clubDetails_btn, judoBelts_btn, groupSessions_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        clubDetails_btn = view.findViewById(R.id.clubDetails);
        judoBelts_btn = view.findViewById(R.id.judoBelts);
        groupSessions_btn = view.findViewById(R.id.GroupSessions);

        clubDetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClubDetails.class);
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
                Intent intent = new Intent(getActivity(), GroupSessions.class);
                startActivity(intent);
            }
        });



        return view;
        }
    }

