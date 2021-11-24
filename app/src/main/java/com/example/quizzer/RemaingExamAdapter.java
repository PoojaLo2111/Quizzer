package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RemaingExamAdapter extends RecyclerView.Adapter<RemaingExamAdapter.ViewHolder> {

    private List<RemaingExamItemModel> remaingExamItemModelList;
    private String Class_name;

    public RemaingExamAdapter(List<RemaingExamItemModel> remaingExamItemModelList,String Class_name) {
        this.remaingExamItemModelList = remaingExamItemModelList;
        this.Class_name = Class_name;
    }

    @NonNull
    @Override
    public RemaingExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remaining_quiz_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String RemaingExamName = remaingExamItemModelList.get(position).getRemaingExamName();
        String RemaingExamDate = remaingExamItemModelList.get(position).getRemaingExamDate();
        String RemaingExamTime = remaingExamItemModelList.get(position).getRemaingExamTime();

        holder.setData(RemaingExamName,RemaingExamDate, RemaingExamTime);
    }

    @Override
    public int getItemCount() {
        return remaingExamItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ExamName;
        private TextView ExamDate;
        private TextView ExamTime;
        private ImageView removeQuiz;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.remaing_quiz_name);
            ExamDate = itemView.findViewById(R.id.remaing_quiz_date);
            ExamTime = itemView.findViewById(R.id.remaing_quiz_time);
            removeQuiz = itemView.findViewById(R.id.remove_quiz);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),QuestionListActivity.class);
                    intent.putExtra("class_name", Class_name);
                    intent.putExtra("Quiz_name",remaingExamItemModelList.get(getAdapterPosition()).getRemaingExamName());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        private void setData(String exam_name, String exam_date, String exam_time){
            ExamName.setText(exam_name);
            ExamDate.setText(exam_date);
            ExamTime.setText(exam_time);
            removeQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quiz_name = remaingExamItemModelList.get(getAdapterPosition()).getRemaingExamName();
                    FirebaseFirestore.getInstance().collection("Class")
                            .document(Class_name)
                            .collection("Quiz")
                            .document(quiz_name).delete();
                    remaingExamItemModelList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
