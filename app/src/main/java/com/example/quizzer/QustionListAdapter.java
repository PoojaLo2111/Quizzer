package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QustionListAdapter extends RecyclerView.Adapter<QustionListAdapter.ViewHolder> {

    private List<QuestionListItemModel> questionListItemModelList;
    String Class_name;
    String Quiz_name;

    public QustionListAdapter(List<QuestionListItemModel> questionListItemModelList,String Class_name, String Quiz_name) {
        this.questionListItemModelList = questionListItemModelList;
        this.Class_name = Class_name;
        this.Quiz_name = Quiz_name;
    }

    @NonNull
    @Override
    public QustionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String question = questionListItemModelList.get(position).getQuestion();
        String answer = questionListItemModelList.get(position).getAnswer();

        holder.setData(question,answer,position);
    }

    @Override
    public int getItemCount() {
        return questionListItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Question;
        private TextView Answer;
        private ImageView delete_que;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Question = itemView.findViewById(R.id.question);
            Answer = itemView.findViewById(R.id.answer);
            delete_que = itemView.findViewById(R.id.delete_que);
        }
        private void setData(String question, String answer,int position){
            Question.setText(question);
            Answer.setText(answer);
            delete_que.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("Class")
                            .document(Class_name)
                            .collection("Quiz")
                            .document(Quiz_name)
                            .collection("Questions")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        int i = 0;
                                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                                            if (i == position){
                                                String id = documentSnapshot.getId();
                                                documentSnapshot.getReference().delete();
                                            break;
                                            } else {
                                                i++;
                                            }
                                        }
                                    }
                                }
                            });
                    questionListItemModelList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
