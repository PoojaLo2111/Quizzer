package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.quizzer.QuizListActivity.class_name;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText question, correctAns, optionA, optionB, optionC, optionD;
    private Button add;
    private String Class_name, Quiz_name;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        question = findViewById(R.id.add_que_Question);
        correctAns = findViewById(R.id.add_que_correctAns);
        optionA = findViewById(R.id.add_que_optionA);
        optionB = findViewById(R.id.add_que_optionB);
        optionC = findViewById(R.id.add_que_optionC);
        optionD = findViewById(R.id.add_que_optionD);

        add = findViewById(R.id.add_que_button);

        Class_name = getIntent().getStringExtra("class_name");
        Quiz_name = getIntent().getStringExtra("Quiz_name");

        firebaseFirestore = FirebaseFirestore.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!question.getText().toString().isEmpty()){
                    if (!correctAns.getText().toString().isEmpty()){
                        if (!optionA.getText().toString().isEmpty()){
                            if (!optionB.getText().toString().isEmpty()){
                                if (!optionC.getText().toString().isEmpty()){
                                    if (!optionD.getText().toString().isEmpty()){
                                        if (correctAns.getText().toString().equals(optionA.getText().toString()) || correctAns.getText().toString().equals(optionB.getText().toString()) || correctAns.getText().toString().equals(optionC.getText().toString()) || correctAns.getText().toString().equals(optionD.getText().toString())){
                                            Map<Object,Object> questionMap =new HashMap<>();
                                            questionMap.put("Question",question.getText().toString());
                                            questionMap.put("CorrectAns",correctAns.getText().toString());
                                            questionMap.put("OptionA",optionA.getText().toString());
                                            questionMap.put("OptionB",optionB.getText().toString());
                                            questionMap.put("OptionC",optionC.getText().toString());
                                            questionMap.put("OptionD",optionD.getText().toString());

                                            firebaseFirestore.collection("Class")
                                                    .document(class_name)
                                                    .collection("Quiz")
                                                    .document(Quiz_name)
                                                    .collection("Questions")
                                                    .add(questionMap)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(AddQuestionActivity.this,"Add",Toast.LENGTH_SHORT).show();
                                                                question.setText("");
                                                                correctAns.setText("");
                                                                optionA.setText("");
                                                                optionB.setText("");
                                                                optionC.setText("");
                                                                optionD.setText("");
//                                                                finish();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(AddQuestionActivity.this,"Correct Answer Does Not Match With Option",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(AddQuestionActivity.this,"Enter OptionD",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AddQuestionActivity.this,"Enter OptionC",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddQuestionActivity.this,"Enter OptionB",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddQuestionActivity.this,"Enter OptionA",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddQuestionActivity.this,"Enter Correct Answer",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddQuestionActivity.this,"Enter Question",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}