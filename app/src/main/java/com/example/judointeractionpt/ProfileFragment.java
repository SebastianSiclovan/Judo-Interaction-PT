package com.example.judointeractionpt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.judointeractionpt.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


@SuppressWarnings("ALL")
public class ProfileFragment extends Fragment{

        DatabaseReference reference;
        FirebaseUser user;
        CircularImageView profileImage;
        TextView username, firstname, lastname, email, phonenumber, judoclub;
        String profileID;
        Button uploadImageBtn, editProfileBtn;
        StorageReference storageReference;

        FirebaseFirestore firebaseFirestore;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference profileReference = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });







        profileID = user.getUid();

        username = view.findViewById(R.id.profile_username);
        firstname = view.findViewById(R.id.profile_firstName);
        lastname = view.findViewById(R.id.profile_lastName);
        email = view.findViewById(R.id.profile_email);
        uploadImageBtn = view.findViewById(R.id.profile_uploadImage);
        profileImage = view.findViewById(R.id.profile_image);
        phonenumber = view.findViewById(R.id.profile_phoneNumber);
        judoclub = view.findViewById(R.id.profile_judoClub);
        editProfileBtn = view.findViewById(R.id.profile_editProfile);



        userInformation();

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase Storage
        StorageReference fileReference = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userInformation() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                // Verification if username is empty
                String usernameVerification = user.getUsername();

                if (usernameVerification.isEmpty())
                {
                    usernameVerification = "You don't have a username set, go to edit profile and set it";
                }


                firstname.setText(user.getFirstName());
                lastname.setText(user.getLastName());
                email.setText(user.getEmail());
                username.setText(usernameVerification);
                phonenumber.setText(user.getCountrycode() + " " + user.getPhonenumber_withoutCountryCode());
                judoclub.setText(user.getJudoclub());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}