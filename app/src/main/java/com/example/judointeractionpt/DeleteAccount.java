package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteAccount extends AppCompatActivity {

    FirebaseAuth FireBase_Auth;
    FirebaseUser user;
    EditText user_password;
    TextView updateVerify;
    Button verify_Btn, deleteAccount_Btn;

    String store_password;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    DatabaseReference databaseReference;

    static final String TAG = "DeleteAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        user_password = findViewById(R.id.deleteAccount_Password);
        updateVerify = findViewById(R.id.deleteAccount_updateText);

        verify_Btn = findViewById(R.id.deleteAccount_verifyBtn);
        deleteAccount_Btn = findViewById(R.id.deleteAccount_btn);

        FireBase_Auth = FirebaseAuth.getInstance();
        user = FireBase_Auth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();



        // disable delete account button untill the user complete verify part
        deleteAccount_Btn.setEnabled(false);

        if (user.equals(""))
        {
            Toast.makeText(DeleteAccount.this, "Something went wrong! User detail's not available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteAccount.this, ProfileFragment.class);
            startActivity(intent);
            finish();
        }
        else
        {
            verifyMethod(user);
        }


    }

    private void verifyMethod(FirebaseUser user)
    {
        verify_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                store_password = user_password.getText().toString();

                // Verification if password field is emply
                if (TextUtils.isEmpty(store_password))
                {
                    user_password.setError("Password is required");
                    user_password.requestFocus();
                    return;
                }
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), store_password);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                // disable editText password.
                                user_password.setEnabled(false);


                                // Enable delete account button and disable verify button
                                deleteAccount_Btn.setEnabled(true);
                                verify_Btn.setEnabled(false);

                                // set TextView to see that the user has completed the verification part
                                updateVerify.setText("You just verified. You can delete your account now");
                                Toast.makeText(DeleteAccount.this, "Password has been verified." + "You can delete your account. Be careful, this action is irreversible", Toast.LENGTH_SHORT).show();

                                // Change color of update email button
                                deleteAccount_Btn.setBackgroundTintList(ContextCompat.getColorStateList(DeleteAccount.this, R.color.dark_green));

                                deleteAccount_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showAlertDialog();

                                    }
                                });


                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                } catch (Exception e){
                                    Toast.makeText(DeleteAccount.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }



            }
        });

    }

    private void showAlertDialog() {

        // Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccount.this);
        builder.setTitle("Delete User and Related Data ?");
        builder.setMessage("Do you really want to delete your account ? This action is irreversible");

        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUser(user);

            }
        });

        // Return to User Profile Activity if User presses Cancel Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(DeleteAccount.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // change color of button continue
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));

            }
        });

        // Show the AlertDialog
        alertDialog.show();
    }

    private void deleteUser(FirebaseUser user) {
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    deleteUserData();
                    FireBase_Auth.signOut();
                    Toast.makeText(DeleteAccount.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DeleteAccount.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try {
                        throw task.getException();

                    } catch (Exception e){

                        Toast.makeText(DeleteAccount.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
    // Delete all the data of User from RealTime Database and Storage
    private void deleteUserData() {

        // Delete profile picture
        storageReference = firebaseStorage.getReferenceFromUrl(user.getPhotoUrl().toString());

        //storageReference.delete();
                /*.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "OnSucces: Photo Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

                 */



        // Delete data from RealTime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "OnSucces: User Data Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}