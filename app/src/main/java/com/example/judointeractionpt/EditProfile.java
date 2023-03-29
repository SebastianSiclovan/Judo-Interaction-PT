package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.judointeractionpt.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;

import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    EditText firstname, lastname, phonenumber, username;
    Button saveChanges, backBtn;
    private String fName, lName, pNumber, uName, cCode;
    private DatabaseReference userRef;
    FirebaseUser user;
    CountryCodePicker country_codeP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        username = findViewById(R.id.EditProfile_userName);
        firstname = findViewById(R.id.EditProfile_firstName);
        lastname = findViewById(R.id.EditProfile_lastName);
        phonenumber = findViewById(R.id.EditProfile_phoneNumber);
        backBtn = findViewById(R.id.EditProfile_back);
        saveChanges = findViewById(R.id.EditProfile_saveChanges);

        country_codeP = findViewById(R.id.ccp);
        country_codeP.setContentColor(Color.WHITE);

        userInformation();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting activity to login.
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = username.getText().toString().trim();
                String newFirstName = firstname.getText().toString().trim();
                String newLastName = lastname.getText().toString().trim();


                // Get the selected country code and name
                String countryCode = country_codeP.getSelectedCountryCode();


                String newCountryCode = country_codeP.getSelectedCountryCode();
                String newPhoneNumber = phonenumber.getText().toString().trim();
                String fullPhoneNumber = "+" + countryCode + " " + newPhoneNumber;


                // Verificam daca utilizatorul a modificat campurile
                boolean hasChanged = false;

                if (isValidPhoneNumber(fullPhoneNumber, countryCode)) {
                    // Phone number is valid

                    countryCode = "+" + countryCode;
                    if (!countryCode.equals(cCode))
                    {
                        cCode = countryCode;
                        hasChanged = true;
                    }
                    if (!newPhoneNumber.equals(pNumber))
                    {
                        pNumber = newPhoneNumber;
                        hasChanged = true;
                    }

                } else
                {
                    // Phone nu ismber invalid
                    phonenumber.setError("Please insert a valid phone number");
                    return;
                }


                    if (!newFirstName.equals(fName)) {
                        fName = newFirstName;
                        hasChanged = true;
                }

                if (!newLastName.equals(lName)) {

                    lName = newLastName;
                    hasChanged = true;

                }
                if (!newUserName.equals(uName)) {

                    uName = newUserName;
                    hasChanged = true;

                }


                // Daca nu a modificat niciun camp, afisam un mesaj si iesim din activitate
                if (!hasChanged) {
                    Toast.makeText(EditProfile.this, "Nu ai facut nicio modificare la profil", Toast.LENGTH_SHORT).show();
                   // finish();
                    return;
                }

                // Actualizam datele in baza de date Firebase Realtime
                userRef.child("username").setValue(uName);
                userRef.child("firstname").setValue(fName);
                userRef.child("lastname").setValue(lName);
                userRef.child("phonenumber_withoutCountryCode").setValue(pNumber);
                userRef.child("countrycode").setValue(cCode);

                // Exit activity and display a message for user
                Toast.makeText(EditProfile.this, "Profilul a fost actualizat cu succes", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean isValidPhoneNumber(String fullPhoneNumber, String countryCode) {
        String regex;
        if (countryCode.equals("US")) {
            // Regular expression for US phone numbers
            regex = "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$";
        } else if (countryCode.equals("CA")) {
            // Regular expression for Canadian phone numbers
            regex = "^(\\+?1)?[2-9]\\d{2}[2-9]\\d{6}$";
        } else if (countryCode.equals("GB")) {
            // Regular expression for UK phone numbers
            regex = "^(\\+44|0)7\\d{9}$";
        } else if (countryCode.equals("RO")) {
            // Regular expression for Romanian phone numbers
            regex = "^\\+40\\s?7[0-9]\\s?[0-9]{3}\\s?[0-9]{3}$";
        } else {
            // Regular expression for other countries
            regex = "^\\+" + countryCode + "\\s?[0-9]{1,14}$";
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullPhoneNumber);
        return matcher.matches();
    }


    private void userInformation() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                firstname.setText(user.getFirstName());
                lastname.setText(user.getLastName());

                // Eliminare prefix din numar, EX: +40 740822347 -> 740822347
                //String phoneNumber = user.getPhonenumber();
                //String PhoneNumberWithoutPrefix = phoneNumber.replace("+", ""); // eliminam semnul "+"
                //PhoneNumberWithoutPrefix = phoneNumber.substring(PhoneNumberWithoutPrefix.indexOf(" ") + 2); // eliminam prefixul pana la primul spatiu inclusiv
                String PhoneNumber_withoutCountryCode = user.getPhonenumber_withoutCountryCode();
                phonenumber.setText(PhoneNumber_withoutCountryCode);
                cCode = user.getCountrycode();
                uName = user.getUsername();
                fName = user.getFirstName();
                lName = user.getLastName();
                pNumber = user.getPhonenumber_withoutCountryCode();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}