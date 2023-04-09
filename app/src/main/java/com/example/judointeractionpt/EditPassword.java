package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class EditPassword extends AppCompatActivity {

    FirebaseAuth FireBase_Auth;
    FirebaseUser user;
    EditText user_currentPassword, user_newPassword, user_confirmNewPassword;
    TextView updateVerify;
    Button verify_Btn, changePassword_Btn;
    String store_currentPassword_EnteredByUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        user_currentPassword = findViewById(R.id.EditPassword_currentPassword);
        user_newPassword = findViewById(R.id.EditPassword_newPassword);
        user_confirmNewPassword = findViewById(R.id.EditPassword_newConfirmPassword);

        updateVerify = findViewById(R.id.EditPassword_updateText);

        verify_Btn = findViewById(R.id.EditPassword_verifyBtn);
        changePassword_Btn = findViewById(R.id.EditPassword_changePawwordBtn);

        // disable change Password button, current password side, new password side untill the user complete verify part
        user_newPassword.setEnabled(false);
        user_confirmNewPassword.setEnabled(false);
        changePassword_Btn.setEnabled(false);

        FireBase_Auth = FirebaseAuth.getInstance();
        user = FireBase_Auth.getCurrentUser();

        if (user.equals(""))
        {
            Toast.makeText(EditPassword.this, "Something went wrong! User detail's not available", Toast.LENGTH_SHORT).show();
        }
        else
        {
            verifyMethod(user);
        }




    }

    private void verifyMethod(FirebaseUser user) {
        verify_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                store_currentPassword_EnteredByUser = user_currentPassword.getText().toString();

                // Verification if password field is emply
                if (TextUtils.isEmpty(store_currentPassword_EnteredByUser))
                {
                    user_currentPassword.setError("Current password is required");
                    user_currentPassword.requestFocus();
                    return;
                }
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), store_currentPassword_EnteredByUser);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                // disable editText for current password. Enable new password and confirm password
                                user_currentPassword.setEnabled(false);
                                user_newPassword.setEnabled(true);
                                user_confirmNewPassword.setEnabled(true);

                                // Enable change password button and disable verify button
                                changePassword_Btn.setEnabled(true);
                                verify_Btn.setEnabled(false);

                                // set TextView to see that the user has completed the verification part
                                updateVerify.setText("You just verified. You can change password now");
                                Toast.makeText(EditPassword.this, "Current password has been verified." + "You can set your new password now", Toast.LENGTH_SHORT).show();

                                // Change color of update email button
                                changePassword_Btn.setBackgroundTintList(ContextCompat.getColorStateList(EditPassword.this, R.color.dark_green));

                                changePassword_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        updatePassword(user);

                                    }
                                });


                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                } catch (Exception e){
                                    Toast.makeText(EditPassword.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }



            }
        });
    }

    private void updatePassword(FirebaseUser user) {

        String store_newPassword_EnteredByUser = user_newPassword.getText().toString();
        String store_ConfirmPassword_EnteredByUser = user_confirmNewPassword.getText().toString();

        if (TextUtils.isEmpty(store_newPassword_EnteredByUser))
        {
            user_newPassword.setError("Please enter new password");
            user_newPassword.requestFocus();
        } else if (TextUtils.isEmpty(store_ConfirmPassword_EnteredByUser))
        {
            user_confirmNewPassword.setError("Please re-enter your new password");
            user_confirmNewPassword.requestFocus();
        } else if (!store_newPassword_EnteredByUser.matches(store_ConfirmPassword_EnteredByUser))
        {
            user_confirmNewPassword.setError("Please re-enter same password");
            user_confirmNewPassword.requestFocus();
        } else if (store_currentPassword_EnteredByUser.matches(store_newPassword_EnteredByUser))
        {
            user_newPassword.setError("New password cannot be same as old password");
            user_newPassword.requestFocus();
        } else
        {
            user.updatePassword(store_newPassword_EnteredByUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(EditPassword.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else
                    {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(EditPassword.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }




    }
}