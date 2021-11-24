package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentRemaingExamAdaper extends RecyclerView.Adapter<StudentRemaingExamAdaper.ViewHolder> {

    private List<CompleteExamItemModel> studentRemaingExamModelList;
    private String class_name;

    public StudentRemaingExamAdaper(List<CompleteExamItemModel> studentRemaingExamModelList,String class_name) {
        this.studentRemaingExamModelList = studentRemaingExamModelList;
        this.class_name = class_name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_quiz_item_layout,parent,false);
        return new StudentRemaingExamAdaper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String SRemaingExamName = studentRemaingExamModelList.get(position).getCompleteExamName();
        String SRemaingExamDate = studentRemaingExamModelList.get(position).getCompleteExamDate();
        String SRemaingExamTime = studentRemaingExamModelList.get(position).getCompleteExamTime();

        holder.setData(SRemaingExamName,SRemaingExamDate,SRemaingExamTime);
    }

    @Override
    public int getItemCount() {
        return studentRemaingExamModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ExamName;
        private TextView ExamDate;
        private TextView ExamTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.complete_quiz_name);
            ExamDate = itemView.findViewById(R.id.complete_quiz_date);
            ExamTime = itemView.findViewById(R.id.complete_quiz_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),QuestionActivity.class);
                    intent.putExtra("Class",class_name);
                    intent.putExtra("Quiz",studentRemaingExamModelList.get(getAdapterPosition()).getCompleteExamName());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        private void setData(String exam_name, String exam_date, String exam_time){
            ExamName.setText(exam_name);
            ExamDate.setText(exam_date);
            ExamTime.setText(exam_time);
        }
    }
}
