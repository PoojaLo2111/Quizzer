package com.example.quizzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{

    private List<ClassModel> classModelList;
    private Context context;
    private TeacherFragment teacherFragment;
    private OnNoteLlistener onNoteLlistener;

    public ClassAdapter(List<ClassModel> classModelList, /*TeacherFragment teacherFragment*/Context context,OnNoteLlistener onNoteLlistener) {
        this.classModelList = classModelList;
//        this.teacherFragment = teacherFragment;
        this.context = context;
        this.onNoteLlistener = onNoteLlistener;
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_layout,parent,false);
        return new ViewHolder(view,onNoteLlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        String class_ch = classModelList.get(position).getClass_ch();
        String class_name = classModelList.get(position).getClass_name();

        holder.setData(class_ch,class_name);
    }

    @Override
    public int getItemCount() {
        return classModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private TextView className;
        private TextView classChar;

        OnNoteLlistener onNoteLlistener;

        public ViewHolder(@NonNull View itemView, OnNoteLlistener onNoteLlistener) {
            super(itemView);

            className = itemView.findViewById(R.id.class_name);
            classChar = itemView.findViewById(R.id.class_char);
            this.onNoteLlistener = onNoteLlistener;

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(this);

        }
        private void setData(String class_ch,String class_name){
            classChar.setText(class_ch);
            className.setText(class_name);
        }

        @Override
        public void onClick(View v) {
            onNoteLlistener.onNoteclick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteLlistener.onNoteLongClick(getAdapterPosition());
            return true;
        }
    }
    public interface OnNoteLlistener{
        void onNoteclick(int position);
        void onNoteLongClick(int position);
    }
}
