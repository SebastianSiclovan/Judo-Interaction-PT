package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEmail extends AppCompatActivity {

    FirebaseAuth FireBase_Auth;
    FirebaseUser user;
    TextView updateVerify, currentEmail;
    String user_oldEmail, user_newEmail, user_password;
    EditText password, newEmail;
    Button changeEmail_btn, verify_Btn;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        password = findViewById(R.id.EditEmail_password);
        newEmail = findViewById(R.id.EditEmail_newEmail);

        updateVerify = findViewById(R.id.EditEmail_updateText);

        changeEmail_btn = findViewById(R.id.EditEmail_changeEmailBtn);
        verify_Btn = findViewById(R.id.EditEmail_verifyBtn);

        FireBase_Auth = FirebaseAuth.getInstance();
        user = FireBase_Auth.getCurrentUser();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        // disable change email button until the not verified his profile
        changeEmail_btn.setEnabled(false);
        // in the same mode as above
        newEmail.setEnabled(false);


        // set old email into the text view
        user_oldEmail = user.getEmail();
        currentEmail = findViewById(R.id.EditEmail_currentEmail);
        currentEmail.setText(user_oldEmail);

        if (user.equals(""))
        {
            Toast.makeText(EditEmail.this, "Something went wrong! User detail's not available", Toast.LENGTH_SHORT).show();
        }
        else
        {
            verifyMethod(user);
        }





    }

    // verify user before change email
    private void verifyMethod(FirebaseUser user) {

        verify_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // obtain password for verify
                user_password = password.getText().toString();

                // Verification if password field is emply
                if (TextUtils.isEmpty(user_password))
                {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(user_oldEmail, user_password);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful())
                           {
                               Toast.makeText(EditEmail.this, "Password has been verified." + "You can change email now", Toast.LENGTH_SHORT).show();

                               // set TextView to see that the user has completed the verification part
                               updateVerify.setText("You just verified. You can change email now");

                               // Disable EditText for password, button to very user and enable EditText for new email and enable change button for email
                               newEmail.setEnabled(true);
                               changeEmail_btn.setEnabled(true);
                               password.setEnabled(false);
                               verify_Btn.setEnabled(false);

                               // Change color of update email button
                               changeEmail_btn.setBackgroundTintList(ContextCompat.getColorStateList(EditEmail.this, R.color.dark_green));

                               changeEmail_btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       user_newEmail = newEmail.getText().toString();

                                       // Verification if password field is emply
                                       if (TextUtils.isEmpty(user_newEmail))
                                       {
                                           newEmail.setError("New email is required");
                                           newEmail.requestFocus();
                                           return;
                                       }
                                       // verify if the user has insert a valid email adress
                                       else if (!Patterns.EMAIL_ADDRESS.matcher(user_newEmail).matches())
                                       {
                                           newEmail.setError("Please insert a valid email adress");
                                           newEmail.requestFocus();
                                       }
                                       else if (user_oldEmail.matches(user_newEmail))
                                       {
                                           newEmail.setError("New email cannot be same as old Email");
                                           newEmail.requestFocus();
                                       }
                                       else
                                       {
                                           updateEmail(user);
                                       }


                                   }
                               });

                           }
                           else
                           {
                               try
                               {
                                   throw task.getException();

                               } catch (Exception e)
                               {
                                   Toast.makeText(EditEmail.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                               }

                           }

                        }
                    });

                }



            }
        });
    }

    private void updateEmail(FirebaseUser user) {
        user.updateEmail(user_newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isComplete())

                {
                    user.sendEmailVerification();
                    Toast.makeText(EditEmail.this, "Email has been changed. Please verify your new email adress", Toast.LENGTH_SHORT).show();

                    // update email in real time database
                    userRef.child("email").setValue(user_newEmail);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else
                {
                    try {
                        task.getException();
                    } catch (Exception e)
                    {
                        Toast.makeText(EditEmail.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}