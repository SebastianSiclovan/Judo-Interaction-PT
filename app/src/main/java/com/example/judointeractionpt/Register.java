package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    // Variabilele required
    public
        EditText my_email, my_userName, my_Password, my_firstName, my_lastName, my_phoneNumber;
        Button my_RegisterBtn;
        TextView my_LoginBtn;
        FirebaseAuth FireBase_Auth;
        ProgressBar progressBar;
        boolean my_passwordVisible;
        DatabaseReference reference;
        CountryCodePicker country_codeP;
        PhoneNumberUtil phoneNumberUtil;
        PhoneNumberUtil phoneUtil;
        AutoCompleteTextView judoClubAutocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Make a connection between class atributes and XML resources

        my_email = findViewById(R.id.email);
        my_userName = findViewById(R.id.userName);
        my_Password = findViewById(R.id.password);
        my_LoginBtn = findViewById(R.id.alreadyBold);
        my_RegisterBtn = findViewById(R.id.registerButton);
        my_firstName = findViewById(R.id.register_firstName);
        my_lastName = findViewById(R.id.register_lastName);
        my_phoneNumber = findViewById(R.id.phoneNumber);

        country_codeP = findViewById(R.id.ccp);
        country_codeP.setContentColor(Color.WHITE);

        my_phoneNumber = findViewById(R.id.phoneNumber);

        judoClubAutocomplete = findViewById(R.id.judo_clubs);

        // The following code will offer posibility of a user to select the judo club in that he is
        // Storage of club will be in an array which are in strings.xml (res/values/strings.xml)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.judo_clubs));
        judoClubAutocomplete.setAdapter(adapter);
        judoClubAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedClub = parent.getItemAtPosition(position).toString();
                // Store the selected club in your register class or EditText field
            }
        });







        // Getting instante for PhoneNumberUtil tool

        // Getting instance from the FireBase
        FireBase_Auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar);

        // Verification is user is already logged in or not

        if (FireBase_Auth.getCurrentUser() != null) // verify this user is present
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // redirect the user from register to login if he press the "Sign In"

        my_LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting activity to login.
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


        my_Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= my_Password.getRight() - my_Password.getCompoundDrawables()[right].getBounds().width()) {
                        int selection = my_Password.getSelectionEnd();
                        if (my_passwordVisible) {
                            //
                            my_Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            //
                            my_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            my_passwordVisible = false;

                        } else {
                            //
                            my_Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                            //
                            my_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            my_passwordVisible = true;
                        }
                        my_Password.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });
        my_RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store the email and password
                String email = my_email.getText().toString().trim();
                String password = my_Password.getText().toString().trim();
                String username = my_userName.getText().toString().trim();
                String firstName = my_firstName.getText().toString().trim();
                String lastName = my_lastName.getText().toString().trim();
                String judoClubs = judoClubAutocomplete.getText().toString().trim();

                // Get the selected country code and name
                String countryCode = country_codeP.getSelectedCountryCode();
                String countryName = country_codeP.getSelectedCountryName();

                // Use the selected country code and phone number to create a full phone number
                String phoneNumber_withoutCountryCode = my_phoneNumber.getText().toString();
                String fullPhoneNumber = "+" + countryCode + " " + phoneNumber_withoutCountryCode;


                // Country code for push in firebase
                String countryCode_firebase = country_codeP.getSelectedCountryCode();
                countryCode_firebase = "+" + countryCode_firebase;

                if (isValidPhoneNumber(fullPhoneNumber, countryCode)) {
                    // Phone number is valid
                } else {
                    // Phone nu ismber invalid
                    my_phoneNumber.setError("Please insert a valid phone number");
                    return;
                }


                // Verification if Full name field is empty

                if (TextUtils.isEmpty(username)) {
                    my_userName.setError("Please complete the full name field");
                    return;
                }

                // Verification if email field is empty
                else if (TextUtils.isEmpty(email)) {
                    my_email.setError("Please complete the email field");
                    return;
                }

                // Verification if password field is emply
                else if (TextUtils.isEmpty(password)) {
                    my_Password.setError("Please complete the password field");
                    return;
                } else if (TextUtils.isEmpty(firstName)) {
                    my_firstName.setError("Please complete the first name field");
                }
                else if (TextUtils.isEmpty(lastName))
                {
                    my_lastName.setError("Please complete the last name field");
                    return;
                }
                else if (TextUtils.isEmpty(judoClubs))
                {
                    judoClubAutocomplete.setError("Please select a judo club");
                    return;
                }
                // Verification if password is GT 6 characters
                else if (password.length() < 6) {
                    my_Password.setError("Please enter password >= 6 characters");
                    return;
                } else {
                    registerUser(username, firstName, lastName, email, fullPhoneNumber, judoClubs, password, countryCode_firebase, phoneNumber_withoutCountryCode);
                }


                // Register the user in FireBase
            }
        });
    }
    // function for verification if a number is valid or not
    private boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
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
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    private void registerUser(String username, String firstName, String lastName, String email, String fullPhoneNumber, String judoClubs, String password, String countryCode_firebase, String phoneNumber_withoutCountryCode) {


                FireBase_Auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        // Push registration data in real time database, only the user have complete register with succes
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", FireBase_Auth.getCurrentUser().getUid());
                        hashMap.put("username", username);
                        hashMap.put("firstname", firstName);
                        hashMap.put("lastname", lastName);
                        hashMap.put("email", email);
                        //hashMap.put("phonenumber", fullPhoneNumber);
                        hashMap.put("countrycode", countryCode_firebase);
                        hashMap.put("phonenumber_withoutCountryCode", phoneNumber_withoutCountryCode);
                        hashMap.put("judoclub", judoClubs);



                        reference.child("Users").child(FireBase_Auth.getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    FireBase_Auth.getCurrentUser().sendEmailVerification();
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(Register.this, "Your account has been created. Please check your email for verification !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Register.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });


            }

        }

