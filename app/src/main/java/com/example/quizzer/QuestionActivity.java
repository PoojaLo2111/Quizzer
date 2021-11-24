package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.quizzer.QuizListActivity.class_name;

public class QuestionActivity extends AppCompatActivity {

    private TextView question,indicator;
    private LinearLayout optionContainor;
    private Button nextButton;
    private int count = 0,position = 0;
    List<QuestionModelClass> list;
    private int score = 0;
    private String class_name,quiz_name;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String stu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question_text);
        indicator = findViewById(R.id.indicator);
        optionContainor = findViewById(R.id.options_container);
        nextButton = findViewById(R.id.next_button);
        class_name = getIntent().getStringExtra("Class");
        quiz_name = getIntent().getStringExtra("Quiz");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("Student")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            stu_id = documentSnapshot.getString("Email");
                        }
                    }
                });

        list = new ArrayList<>();

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .document(quiz_name)
                .collection("Questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String question = documentSnapshot.getString("Question");
                                String optionA = documentSnapshot.getString("OptionA");
                                String optionB = documentSnapshot.getString("OptionB");
                                String optionC = documentSnapshot.getString("OptionC");
                                String optionD = documentSnapshot.getString("OptionD");
                                String correctAns = documentSnapshot.getString("CorrectAns");
                                list.add(new QuestionModelClass(question,optionA,optionB,optionC,optionD,correctAns));
                            }
                            if (list.size() > 0){
                                for (int i = 0;i<4;i++){
                                    optionContainor.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAns(((Button)view).getText().toString());
                                        }
                                    });
                                }
                                playAnim(question,0,list.get(position).getQuestion());

                                nextButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nextButton.setEnabled(false);
                                        position++;
                                        enableOption(true);
                                        if (position == list.size()){
                                            Map<Object,Object> score_data =new HashMap<>();
                                            score_data.put("Marks",score);
                                            score_data.put("std_id",stu_id);
                                            score_data.put("Total",String.valueOf(list.size()));

//                                            Toast.makeText(QuestionActivity.this,String.valueOf(score),Toast.LENGTH_SHORT).show();


//                                            firebaseFirestore.collection("Class")
//                                                    .document(class_name)
//                                                    .collection("Quiz")
//                                                    .document(quiz_name)
//                                                    .collection("Marks")
//                                                    .get()
//                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                            if(task.isSuccessful()){
//                                                                for (DocumentSnapshot documentSnapshot : task.getResult()){
//                                                                    String id = documentSnapshot.getString("std_id");
//                                                                    Toast.makeText(QuestionActivity.this,"1",Toast.LENGTH_SHORT).show();
//                                                                    if (stu_id.equals(id)){
//                                                                        Toast.makeText(QuestionActivity.this,"already",Toast.LENGTH_SHORT).show();
//                                                                    } else {
//                                                                        Toast.makeText(QuestionActivity.this,"Create",Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    });

                                            firebaseFirestore.collection("Class")
                                                    .document(class_name)
                                                    .collection("Quiz")
                                                    .document(quiz_name)
                                                    .collection("Marks")
                                                    .add(score_data)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(QuestionActivity.this,String.valueOf(score),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

//                                            firebaseFirestore.collection("Class")
//                                                    .document(class_name)
//                                                    .collection("Quiz")
//                                                    .document(quiz_name)
//                                                    .collection("Marks")
//                                                    .whereNotEqualTo("std_id",stu_id)
//                                                    .whereEqualTo("std_id",stu_id)
//                                                    .get()
//                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                            Toast.makeText(QuestionActivity.this,"You already gave this exam so score can't added",Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }) . addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(QuestionActivity.this,"Add data",Toast.LENGTH_SHORT).show();
//
//                                                            firebaseFirestore.collection("Class")
//                                                                    .document(class_name)
//                                                                    .collection("Quiz")
//                                                                    .document(quiz_name)
//                                                                    .collection("Marks")
//                                                                    .add(score_data)
//                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                                                                            if (task.isSuccessful()) {
//                                                                                Toast.makeText(QuestionActivity.this, String.valueOf(score), Toast.LENGTH_SHORT).show();
//
//                                                                            }
//                                                                        }
//                                                                    });
//                                                        }
//                                                    });

//                                            Map<Object,Object> s_score =new HashMap<>();
//                                            score.put("Marks",score);
//                                            score.put("test",quiz_name);
//
//                                            firebaseFirestore.collection("Student")
//                                                    .document(firebaseAuth.getCurrentUser().getUid())
//                                                    .collection("Class")
//                                                    .document(class_name)
//                                                    .collection("Quiz marks")
//                                                    .document(quiz_name)
//                                                    .set(s_score)
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void unused) {
//
//                                                        }
//                                                    });

                                            finish();
                                            return;
                                        }
                                        count = 0;
                                        playAnim(question,0,list.get(position).getQuestion());
                                    }
                                });
                            } else {
                                Toast.makeText(QuestionActivity.this,"No Question",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

//        question.setText(list.get(0).getQuestion());
//        optionA.setText(list.get(0).getOptionA());
//        optionB.setText(list.get(0).getOptionB());
//        optionC.setText(list.get(0).getOptionC());
//        optionD.setText(list.get(0).getOptionD());

        /*list.add(new QuestionModelClass("Question1","A","B","C","D","A"));
        list.add(new QuestionModelClass("Question2","A","B","C","D","C"));
        list.add(new QuestionModelClass("Question3","A","B","C","D","B"));
        list.add(new QuestionModelClass("Question4","A","B","C","D","A"));
        list.add(new QuestionModelClass("Question5","A","B","C","D","D"));
        list.add(new QuestionModelClass("Question6","A","B","C","D","C"));
        list.add(new QuestionModelClass("Question7","A","B","C","D","B"));
        list.add(new QuestionModelClass("Question8","A","B","C","D","A"));
        list.add(new QuestionModelClass("Question9","A","B","C","D","A"));
        list.add(new QuestionModelClass("Question10","A","B","C","D","C"));*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        score = 0;
        Map<Object,Object> score_data =new HashMap<>();
        score_data.put("Marks",score);
        score_data.put("std_id",stu_id);
        score_data.put("Total",String.valueOf(list.size()));
        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .document(quiz_name)
                .collection("Marks")
                .add(score_data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(QuestionActivity.this,String.valueOf(score),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void playAnim(View view , int value, String data){
        view.animate().alpha(value).scaleX(value).scaleY(value)
                .setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if (value == 0 && count<4){
                            String option= "";
                            if (count == 0){
                                option = list.get(position).getOptionA();
                            } else if (count == 1){
                                option = list.get(position).getOptionB();
                            } else if (count == 2){
                                option = list.get(position).getOptionC();
                            } else if (count == 3){
                                option = list.get(position).getOptionD();
                            }
                            playAnim(optionContainor.getChildAt(count),0,option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        try {
                            ((TextView)view).setText(data);
                            indicator.setText(position+1+"/"+list.size());
                        } catch (ClassCastException ex){
                            ((Button)view).setText(data);
                        }
                        view.setTag(data);
                        playAnim(view,1,data);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    private void checkAns(String selectedOption){
        enableOption(false);
        nextButton.setEnabled(true);
        if (selectedOption.equals(list.get(position).getCorrectAns())){
            score++;
        } else {

        }
    }

    private void enableOption(boolean enable){
        for (int i = 0;i<4;i++){
            optionContainor.getChildAt(i).setEnabled(enable);
            if (enable){
                optionContainor.setEnabled(true);
            }
        }
    }
}