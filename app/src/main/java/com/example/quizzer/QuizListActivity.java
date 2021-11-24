package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class QuizListActivity extends AppCompatActivity{

    public static String class_name;
    private Button addStudentsButton;
    private EditText addstudentEditText;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ImageView stdGroup;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        addstudentEditText = findViewById(R.id.add_student);
        addStudentsButton = findViewById(R.id.add_button);
        stdGroup = findViewById(R.id.student_list_icon);
        class_name =getIntent().getStringExtra("Class");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabPageAdapter tabPageAdapter = new TabPageAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(tabPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        stdGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(QuizListActivity.this,StudentListActivity.class);
                mainIntent.putExtra("class",class_name);
                startActivity(mainIntent);
            }
        });

        addStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    firebaseFirestore.collection("Class")
                            .document(class_name)
                            .collection("Students")
                            .whereEqualTo("Std_Email",addstudentEditText.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        boolean i = false;
                                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                                            i = true;
                                        }
                                        Log.d(TAG,"song"+i);
                                        if (i){
                                            Toast.makeText(QuizListActivity.this,"Student Already Added",Toast.LENGTH_SHORT).show();
                                        } else {
                                            firebaseFirestore
                                                    .collection("Student")
                                                    .whereEqualTo("Email",addstudentEditText.getText().toString())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                                                Map<Object,String> stu_email =new HashMap<>();
                                                                stu_email.put("Std_Email",documentSnapshot.getString("Email"));
                                                                stu_email.put("std_id",documentSnapshot.getId());

                                                                Map<Object,String> stu_class =new HashMap<>();
                                                                stu_class.put("Class",class_name);

                                                                firebaseFirestore.collection("Class")
                                                                        .document(class_name)
                                                                        .collection("Students")
                                                                        .add(stu_email)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                firebaseFirestore.collection("Student")
                                                                                        .document(documentSnapshot.getId())
                                                                                        .collection("Class")
                                                                                        .document(class_name)
                                                                                        .set(stu_class)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Toast.makeText(QuizListActivity.this,"Student added",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                            });
                                        }
                                    }
                                }
                            });
            }
        });
    }

    /*@Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month/day/year: " + month + "/" + dayOfMonth + "/" + year;
        TextView setDate = dialog.findViewById(R.id.quiz_date);
        setDate.setText(date);
    }*/

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(QuizListActivity.this,MainActivity2.class);
        startActivity(mainIntent);
        finish();
    }

}