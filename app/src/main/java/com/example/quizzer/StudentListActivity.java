package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class StudentListActivity extends AppCompatActivity implements StudentListAdapter.OnNoteLlistener{

    private RecyclerView studentListRecyclerView;
    private List<StudentListItemModel> studentListItemModelList;
    private StudentListAdapter studentListAdapter;
    private static String class_name;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentListRecyclerView = findViewById(R.id.student_list_recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        class_name =getIntent().getStringExtra("class");

        studentListItemModelList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        studentListAdapter = new StudentListAdapter(studentListItemModelList, StudentListActivity.this,this);
        studentListRecyclerView.setAdapter(studentListAdapter);
        studentListRecyclerView.setLayoutManager(layoutManager);

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Students")
                .orderBy("Std_Email")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String std_mail = documentSnapshot.getString("Std_Email");
                                studentListItemModelList.add(new StudentListItemModel(std_mail));
                            }
                            studentListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onNoteclick(int position) {

        String email = studentListItemModelList.get(position).getStd_name();

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Students")
                .whereEqualTo("Std_Email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getString("std_id");

                                firebaseFirestore.collection("Class")
                                        .document(class_name)
                                        .collection("Students")
                                        .document(documentSnapshot.getId())
                                        .delete();
                                firebaseFirestore.collection("Student")
                                        .document(id)
                                        .collection("Class")
                                        .document(class_name)
                                        .delete();

                            }
                            studentListAdapter.notifyDataSetChanged();
                        }
                    }
                });
        studentListItemModelList.remove(position);
    }
}