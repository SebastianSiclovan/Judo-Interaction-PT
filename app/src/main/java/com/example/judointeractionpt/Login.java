package com.example.judointeractionpt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private static final String FILE_NAME = "myFile";
    private static final String TAG = "Login";
    // required variables
    EditText my_email, my_password;
    Button my_loginBtn;
    TextView my_RegisterBtn, my_forgotPassword;
    ProgressBar progressBar;

    FirebaseAuth FireBase_Auth;
    FirebaseUser user;
    CheckBox my_remember;

   // CallbackManager callbackManager;


    SharedPreferences my_Preferences;
    SharedPreferences.Editor my_editor;

    // Check the shared preferences and set them accordingly (in consecinta)
    void checkSharedPreferences()
    {
        // Store this useful string into "string.xml" file to use declared strings in this file.
        String mRemember = my_Preferences.getString(getString(R.string.mRemember), "False");
        String mEmail = my_Preferences.getString(getString(R.string.mEmail), "");
        String mPassword = my_Preferences.getString(getString(R.string.mPassword), "");

        my_email.setText(mEmail);
        my_password.setText(mPassword);

        if(mRemember.equals("True")){
            my_remember.setChecked(true); // setting checked
        } else{
            my_remember.setChecked(false); // setting unchecked
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FacebookSdk.sdkInitialize(getApplicationContext());

        // Make a connection between our required variables and XML resources

        my_email = findViewById(R.id.Login_email);
        progressBar = findViewById(R.id.Login_progressBar);
        my_RegisterBtn = findViewById(R.id.Login_BoldsingUp);
        my_password = findViewById(R.id.Login_password);
        my_loginBtn = findViewById(R.id.Login_Button);
        my_forgotPassword = findViewById(R.id.Login_forgotPassword);
        my_remember = findViewById(R.id.Login_remember);

        // declare shared preferences, king of a dataBase
        my_Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // with editor we can store all things used into preferences shared, kind of a tool used to store items in dataBase
        my_editor = my_Preferences.edit();

        // called this method for shared preferences
        checkSharedPreferences();



        // Login the user, Firebase instance
        FireBase_Auth = FirebaseAuth.getInstance();












        my_RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting activity to login.
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                //startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });





        // Implement click on the Login button
        my_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = my_email.getText().toString().trim();
                String password = my_password.getText().toString().trim();

                // Condition for save the email and password

                if(my_remember.isChecked()){
                    // Set a checkbox when the application starts
                    my_editor.putString(getString(R.string.mRemember), "True");
                    my_editor.commit();

                    // save email
                    my_editor.putString(getString(R.string.mEmail), email);
                    my_editor.commit();

                    // save password
                    my_editor.putString(getString(R.string.mPassword), password);
                    my_editor.commit();

                }else{

                    // Set a checkbox when the application starts
                    my_editor.putString(getString(R.string.mRemember), "False");
                    my_editor.commit();

                    // not save email
                    my_editor.putString(getString(R.string.mEmail), "");
                    my_editor.commit();

                    // not save password
                    my_editor.putString(getString(R.string.mPassword), "");
                    my_editor.commit();


                }




                // Verification if email field is empty
                if (TextUtils.isEmpty(email)) {
                    my_email.setError("Please complete the email field");
                    return;
                }

                // Verification if password field is emply
                if (TextUtils.isEmpty(password)) {
                    my_password.setError("Please complete the password field");
                    return;
                }

                // Verification if password is GT 6 characters
                if (password.length() < 6) {
                    my_password.setError("Please enter a password >= 6 characters");
                }

                // Starting the progress bar to inform the user is in a process of registration
                progressBar.setVisibility(View.VISIBLE);



                FireBase_Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (FireBase_Auth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(Login.this, "Login failed. Please verify your email address!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });



        // redirect the user from login to register if he press the "Sign Up"


        // Let the user to press on "Forgot Password ?" if he forgot the password
        my_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Rese`t Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        FireBase_Auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }

}

