package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompleteExamAdapter extends RecyclerView.Adapter<CompleteExamAdapter.ViewHolder>{

    private List<CompleteExamItemModel> completeExamItemModelList;
    private String class_name;

    public CompleteExamAdapter(List<CompleteExamItemModel> completeExamItemModelList, String class_name) {
        this.completeExamItemModelList = completeExamItemModelList;
        this.class_name = class_name;
    }

    @NonNull
    @Override
    public CompleteExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_quiz_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteExamAdapter.ViewHolder holder, int position) {
        String CompleteExamName = completeExamItemModelList.get(position).getCompleteExamName();
        String CompleteExamDate = completeExamItemModelList.get(position).getCompleteExamDate();
        String CompleteExamTime = completeExamItemModelList.get(position).getCompleteExamTime();

        holder.setData(CompleteExamName,CompleteExamDate,CompleteExamTime);
    }

    @Override
    public int getItemCount() {
        return completeExamItemModelList.size();
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
                    Intent intent = new Intent(itemView.getContext(),StudentsMarksActivity.class);
                    intent.putExtra("class_name", class_name);
                    intent.putExtra("Quiz_name",completeExamItemModelList.get(getAdapterPosition()).getCompleteExamName());
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
