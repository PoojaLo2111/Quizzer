package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentsMarksActivity extends AppCompatActivity {

    private RecyclerView studentMarksListRecyclerView;
    private List<StudentMarksLIstItemModel> studentMarksLIstItemModelList;
    private StudentMarksListAdapter studentMarksListAdapter;
    private static String class_name;
    private static String quizName;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_marks);

        studentMarksListRecyclerView = findViewById(R.id.student_marks_list_recycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        studentMarksLIstItemModelList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        studentMarksListAdapter = new StudentMarksListAdapter(studentMarksLIstItemModelList);
        studentMarksListRecyclerView.setAdapter(studentMarksListAdapter);
        studentMarksListRecyclerView.setLayoutManager(layoutManager);

        class_name =getIntent().getStringExtra("class_name");
        quizName = getIntent().getStringExtra("Quiz_name");

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .document(quizName)
                .collection("Marks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getString("std_id");
                                String std_marks = documentSnapshot.getString("Marks");
                                String total = documentSnapshot.getString("Total");

                                studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel(id,std_marks+"/"+total));
                            }
                            studentMarksListAdapter.notifyDataSetChanged();
                        }
                    }
                });

        /*studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel("18It053","9/10"));
        studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel("18It053","9/10"));
        studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel("18It053","9/10"));
        studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel("18It053","9/10"));
        studentMarksLIstItemModelList.add(new StudentMarksLIstItemModel("18It053","9/10"));

        studentMarksListAdapter.notifyDataSetChanged();*/

    }
}