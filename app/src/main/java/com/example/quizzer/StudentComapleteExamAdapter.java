package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentComapleteExamAdapter extends RecyclerView.Adapter<StudentComapleteExamAdapter.ViewHolder> {

    private List<StudentComapleteExamItemModel> studentComapleteExamItemModelList;
    private String Class_name;

    public StudentComapleteExamAdapter(List<StudentComapleteExamItemModel> studentComapleteExamItemModelList, String class_name) {
        this.studentComapleteExamItemModelList = studentComapleteExamItemModelList;
        Class_name = class_name;
    }

    @NonNull
    @Override
    public StudentComapleteExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_complete_quiz_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String StudentCompleteExamName = studentComapleteExamItemModelList.get(position).getCompleteExamName();
        String StudentCompleteExamDate = studentComapleteExamItemModelList.get(position).getCompleteExamDate();
        String StudentCompleteExamTime = studentComapleteExamItemModelList.get(position).getCompleteExamTime();
        String StudentCompleteExamMarks = studentComapleteExamItemModelList.get(position).getCompleteExamMarks();

        holder.setData(StudentCompleteExamName,StudentCompleteExamDate, StudentCompleteExamTime,StudentCompleteExamMarks);
    }

    @Override
    public int getItemCount() {
        return studentComapleteExamItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ExamName;
        private TextView ExamDate;
        private TextView ExamTime;
        private TextView ExamMarks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.student_complete_quiz_name);
            ExamDate = itemView.findViewById(R.id.student_complete_quiz_date);
            ExamTime = itemView.findViewById(R.id.student_complete_quiz_time);
            ExamMarks = itemView.findViewById(R.id.student_complete_quiz_marks);
        }
        private void setData(String exam_name, String exam_date, String exam_time, String exam_marks) {
            ExamName.setText(exam_name);
            ExamDate.setText(exam_date);
            ExamTime.setText(exam_time);
            ExamMarks.setText(exam_marks);
        }
    }
}
