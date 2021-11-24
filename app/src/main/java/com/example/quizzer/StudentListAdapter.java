package com.example.quizzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<StudentListItemModel> studentListItemModelList;
    private Context context;
    private OnNoteLlistener onNoteLlistener;

    public StudentListAdapter(List<StudentListItemModel> studentListItemModelList, Context context, OnNoteLlistener onNoteLlistener) {
        this.studentListItemModelList = studentListItemModelList;
        this.context = context;
        this.onNoteLlistener = onNoteLlistener;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item_layout,parent,false);
        return new ViewHolder(view,onNoteLlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {
        String stdName = studentListItemModelList.get(position).getStd_name();
        holder.setData(stdName);
    }

    @Override
    public int getItemCount() {
        return studentListItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView student_name;
        private ImageButton delete_image;
        OnNoteLlistener onNoteLlistener;

        public ViewHolder(@NonNull View itemView, OnNoteLlistener onNoteLlistener) {
            super(itemView);

            student_name = itemView.findViewById(R.id.std_email);
            delete_image = itemView.findViewById(R.id.deleteimagebutton);
            this.onNoteLlistener = onNoteLlistener;

            delete_image.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteLlistener.onNoteclick(getAdapterPosition());
        }

        public void setData(String stdName) {
            student_name.setText(stdName);
        }
    }
    public interface OnNoteLlistener{
        void onNoteclick(int position);
    }
}
