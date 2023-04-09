package com.example.judointeractionpt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class SettingsFragment extends Fragment {

    LinearLayout logout_btn;
    LinearLayout changePassword_btn;
    LinearLayout changeEmail_btn;

    FirebaseAuth FireBase_Auth;
    FirebaseUser user;



    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        logout_btn = view.findViewById(R.id.logout);
        changePassword_btn = view.findViewById(R.id.changePassword);
        changeEmail_btn = view.findViewById(R.id.changeEmail);

        // Login the user, Firebase instance
        FireBase_Auth = FirebaseAuth.getInstance();

        user = FireBase_Auth.getCurrentUser();


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        changeEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditEmail.class);
                startActivity(intent);
            }
        });

        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPassword.class);
                startActivity(intent);
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