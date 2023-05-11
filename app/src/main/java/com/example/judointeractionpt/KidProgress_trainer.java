package com.example.judointeractionpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KidProgress_trainer extends AppCompatActivity {

    EditText kid_name, code, belt_type, qualification, currentWeek, number_presentWeek, number_sessions;
    Button addProgress_btn, deleteProgress_btn;

    TextView addText, deleteText;

    FirebaseAuth FireBase_Auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_progress_trainer);

        kid_name = findViewById(R.id.kidName_trainer);
        code = findViewById(R.id.kidCode_trainer);
        belt_type = findViewById(R.id.kidBelt_Trainer);
        qualification = findViewById(R.id.kidQualification_Trainer);
        currentWeek = findViewById(R.id.kidCurrentWeek_Trainer);
        number_presentWeek = findViewById(R.id.kidPresentWeek_Trainer);
        number_sessions = findViewById(R.id.kidSessions_Trainer);

        addText = findViewById(R.id.kidProgress_addText);
        deleteText = findViewById(R.id.kidProgress_deleteText);

        addProgress_btn = findViewById(R.id.kidProgress_addInfo);
        deleteProgress_btn = findViewById(R.id.kidProgress_deleteInfo);

        // Getting instance from the FireBase
        FireBase_Auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        deleteProgress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(KidProgress_trainer.this, "Enter your child's unique code to delete your child's information!", Toast.LENGTH_SHORT).show();
                kid_name.setVisibility(View.GONE);
                belt_type.setVisibility(View.GONE);
                qualification.setVisibility(View.GONE);
                currentWeek.setVisibility(View.GONE);
                number_presentWeek.setVisibility(View.GONE);
                number_sessions.setVisibility(View.GONE);

                deleteText.setVisibility(View.VISIBLE);
                addText.setVisibility(View.GONE);



                String store_code = code.getText().toString().trim();

                if (TextUtils.isEmpty(store_code)) {
                    code.setError("Please complete the code of kid");
                    return;
                }
                else
                {
                    Toast.makeText(KidProgress_trainer.this, "Information about this child has been deleted", Toast.LENGTH_SHORT).show();
                    reference.child("Kids").child(store_code).removeValue();
                }





            }
        });


        addProgress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kid_name.setVisibility(View.VISIBLE);
                belt_type.setVisibility(View.VISIBLE);
                qualification.setVisibility(View.VISIBLE);
                currentWeek.setVisibility(View.VISIBLE);
                number_presentWeek.setVisibility(View.VISIBLE);
                number_sessions.setVisibility(View.VISIBLE);

                deleteText.setVisibility(View.GONE);
                addText.setVisibility(View.VISIBLE);

                String store_kidName = kid_name.getText().toString().trim();
                String store_code = code.getText().toString().trim();
                String store_beltType = belt_type.getText().toString().trim();
                String store_qualification = qualification.getText().toString().trim();
                String store_currentWeek = currentWeek.getText().toString().trim();
                String store_numberPresentWeeek = number_presentWeek.getText().toString().trim();
                String store_sessions = number_sessions.getText().toString().trim();

                String[] judo_belts_array = new String[] {"blue", "brown", "green", "orange", "yellow", "white"};
                String[] qualify_type = new String[] {"FB", "B", "S", "I"};
                boolean isError_judoBelts = true;
                boolean isError_qualifyType = true;
                String regex = "\\d{2}\\.\\d{2}\\.\\d{4} - \\d{2}\\.\\d{2}\\.\\d{4}";




                if (TextUtils.isEmpty(store_kidName))
                {
                    kid_name.setError("Please complete the kid name field");
                    return;
                }

                if (TextUtils.isEmpty(store_code))
                {
                    code.setError("Please complete the code of kid");
                    return;
                }

                if (TextUtils.isEmpty(store_beltType))
                {
                    belt_type.setError("Please complete the Belt type field");
                    return;
                }

                for (int i = 0; i < judo_belts_array.length; i++) {
                    if (store_beltType.equals(judo_belts_array[i])) {
                        isError_judoBelts = false;
                        break;
                    }
                }
                if (isError_judoBelts || store_beltType.isEmpty()) {
                    belt_type.setError("There is only white, yellow, green, brown or blue belt in judo sport");
                    return;
                }


                if (TextUtils.isEmpty(store_beltType))
                {
                    belt_type.setError("Please complete the Belt type field");
                    return;
                }

                if (TextUtils.isEmpty(store_qualification))
                {
                    qualification.setError("Please complete the qualification field");
                    return;
                }

                if (TextUtils.isEmpty(store_currentWeek))
                {
                    currentWeek.setError("Please complete the Current week field");
                    return;
                }

                if (TextUtils.isEmpty(store_numberPresentWeeek))
                {
                    number_presentWeek.setError("Please complete the Number present week field");
                    return;
                }

                if (TextUtils.isEmpty(store_sessions))
                {
                    number_sessions.setError("Please complete the number of training sessions field");
                    return;
                }

                if (store_numberPresentWeeek.matches("[1-6]"))
                {
                    // is ok
                }
                else
                {
                    // is not ok
                    number_presentWeek.setError("The number of attendees is only between 1-6");
                    return;
                }

                if (store_sessions.matches("[1-6]"))
                {
                    // is ok
                }
                else
                {
                    // is not ok
                    number_sessions.setError("The number of attendees is only between 1-6");
                    return;
                }

                if (Integer.parseInt(store_numberPresentWeeek) > Integer.parseInt(store_sessions))
                {
                    Toast.makeText(KidProgress_trainer.this, "The number of attendees cannot be more than the total number of training sessions in the current week.", Toast.LENGTH_SHORT).show();
                }

                // verificăm dacă textul respectă formatul dorit
                if (!store_currentWeek.matches(regex)) {
                    currentWeek.setError("Intervalul de zile trebuie să fie de forma dd.MM.yyyy - dd.MM.yyyy!");
                    return;
                }

                // transformăm textul în obiecte Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date startDate = null;
                try {
                    startDate = sdf.parse(store_currentWeek.substring(0, 10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date endDate = null;
                try {
                    endDate = sdf.parse(store_currentWeek.substring(13));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // calculăm diferența dintre cele două date
                long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

                // verificăm dacă diferența este de 7 zile
                if (diffInDays != 6) {
                    currentWeek.setError("The interval of days must be exactly 7 days!");
                    return;
                }

                for (int i = 0; i < qualify_type.length; i++) {
                    if (store_qualification.equals(qualify_type[i])) {
                        isError_qualifyType = false;
                        break;
                    }
                }
                if (isError_qualifyType || store_qualification.isEmpty()) {
                    qualification.setError("Existing qualifications are FB, B, S, I");
                    return;
                }

                if(true) {
                    Toast.makeText(KidProgress_trainer.this, "Information about that child has been successfully uploaded to the application", Toast.LENGTH_SHORT).show();
                    SaveData_intoFireBase(store_kidName, store_code, store_beltType, store_currentWeek, store_numberPresentWeeek, store_sessions, store_qualification);
                }



            }
        });






    }

    private void SaveData_intoFireBase(String store_kidName, String store_code, String store_beltType, String store_currentWeek, String store_numberPresentWeeek, String store_sessions, String store_qualification) {


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", store_kidName);
        hashMap.put("code", store_code);
        hashMap.put("belt_type", store_beltType);
        hashMap.put("current_week", store_currentWeek);
        hashMap.put("number_presentWeek", store_numberPresentWeeek);
        hashMap.put("number_sessions",store_sessions);
        hashMap.put("qualification", store_qualification);

        reference.child("Kids").child(store_code).setValue(hashMap);



    }



}