package com.example.judointeractionpt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;




public class SettingsFragment extends Fragment {

    LinearLayout logout_btn;

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        logout_btn = view.findViewById(R.id.logout);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        return view;

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        // Clear session data
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        // Redirect to login screen
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

}