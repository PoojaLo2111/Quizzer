package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentMarksListAdapter extends RecyclerView.Adapter<StudentMarksListAdapter.ViewHolder> {

    private List<StudentMarksLIstItemModel> studentMarksLIstItemModelList;

    public StudentMarksListAdapter(List<StudentMarksLIstItemModel> studentMarksLIstItemModelList) {
        this.studentMarksLIstItemModelList = studentMarksLIstItemModelList;
    }

    @NonNull
    @Override
    public StudentMarksListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_marks_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentMarksListAdapter.ViewHolder holder, int position) {
        String stdMail = studentMarksLIstItemModelList.get(position).getEmail();
        String stdMark = studentMarksLIstItemModelList.get(position).getMarks();
        holder.setData(stdMail,stdMark);
    }

    @Override
    public int getItemCount() {
        return studentMarksLIstItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView std_mail;
        private TextView std_marks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            std_mail = itemView.findViewById(R.id.std_marks_email);
            std_marks = itemView.findViewById(R.id.stu_marks);
        }
        public void setData(String stdMail,String stdMarks) {
            std_mail.setText(stdMail);
            std_marks.setText(stdMarks);
        }
    }
}
