package com.example.quizzer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.quizzer.QuizListActivity.class_name;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemainingExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemainingExamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RemainingExamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemainingExamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemainingExamFragment newInstance(String param1, String param2) {
        RemainingExamFragment fragment = new RemainingExamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FloatingActionButton addQuiz;
    private Dialog dialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    int hr1,min1,hr2,min2;
    int day,mon,years;
    EditText quizName;
    String Class_name;
    private RecyclerView remaingExamRecycleView;
    private List<RemaingExamItemModel> remaingExamItemModelList;
    private RemaingExamAdapter remaingExamAdapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remaining_exam, container, false);
        addQuiz = view.findViewById(R.id.add_quiz);
        firebaseFirestore = FirebaseFirestore.getInstance();
        remaingExamRecycleView = view.findViewById(R.id.remaing_exam_recycleView);
        remaingExamItemModelList = new ArrayList<>();
        remaingExamAdapter = new RemaingExamAdapter(remaingExamItemModelList,class_name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        remaingExamRecycleView.setLayoutManager(layoutManager);
        remaingExamRecycleView.setAdapter(remaingExamAdapter);

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String name = documentSnapshot.getString("Quiz_Name");
                                String day = documentSnapshot.getString("Day");
                                String month = documentSnapshot.getString("Month");
                                String year = documentSnapshot.getString("Year");
                                String hr1 = documentSnapshot.getString("hr1");
                                String min1 = documentSnapshot.getString("min1");
                                String hr2 = documentSnapshot.getString("hr2");
                                String min2 = documentSnapshot.getString("min2");

                                int tempDay = Integer.parseInt(day);
                                int tempMonth = Integer.parseInt(month);
                                int tempYear = Integer.parseInt(year);
                                int temphr = Integer.parseInt(hr2);
                                int tempmin = Integer.parseInt(min2);

                                int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                                int m = Calendar.getInstance().get(Calendar.MONTH)+1;
                                int y = Calendar.getInstance().get(Calendar.YEAR);
                                int hr = Calendar.getInstance().get(Calendar.HOUR);
                                int min = Calendar.getInstance().get(Calendar.MINUTE);
                                int am_pm = Calendar.getInstance().get(Calendar.AM_PM);

                                if (am_pm == 1){
                                    hr = hr + 12;
                                }

                                if (!((tempYear<y) || (tempYear==y && tempMonth<m) || (tempYear==y && tempMonth==m && tempDay<d) || (tempYear==y && tempMonth==m && tempDay==d && temphr<hr && tempmin<min))){
                                    remaingExamItemModelList.add(new RemaingExamItemModel(name,day+"/"+month+"/"+year,hr1+":"+min1+" - "+hr2+":"+min2));
                                }
//                                remaingExamItemModelList.add(new RemaingExamItemModel(name,day+"/"+month+"/"+year,hr1+":"+min1+" - "+hr2+":"+min2));
                            }
                            remaingExamAdapter.notifyDataSetChanged();
                        }
                    }
                });

        /*remaingExamItemModelList.add(new RemaingExamItemModel("Name","Date", "Time"));
        remaingExamItemModelList.add(new RemaingExamItemModel("Name","Date", "Time"));
        remaingExamItemModelList.add(new RemaingExamItemModel("Name","Date", "Time"));
        remaingExamItemModelList.add(new RemaingExamItemModel("Name","Date", "Time"));
        remaingExamItemModelList.add(new RemaingExamItemModel("Name","Date", "Time"));

        remaingExamAdapter.notifyDataSetChanged();*/
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_quiz_dailog);

        quizName = dialog.findViewById(R.id.new_quiz_name);
        ImageButton setDate = dialog.findViewById(R.id.set_quiz_date);
        ImageButton setStartTime = dialog.findViewById(R.id.set_start_quiz_time);
        ImageButton setEndTime = dialog.findViewById(R.id.set_end_quiz_time);
        Button create = dialog.findViewById(R.id.create_quiz);
        Button cancle = dialog.findViewById(R.id.cancel_quiz);
        TextView quizDate = dialog.findViewById(R.id.quiz_date);
        TextView quizStartTime = dialog.findViewById(R.id.start_quiz_time);
        TextView quizEndTime = dialog.findViewById(R.id.end_quiz_time);

        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (!quizName.getText().toString().isEmpty()){
                    if (!quizDate.getText().toString().isEmpty()){
                        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        int m = Calendar.getInstance().get(Calendar.MONTH)+1;
                        int y = Calendar.getInstance().get(Calendar.YEAR);

                        if ((years>y) || (years==y && mon>m) || (years==y && mon==m && day>d) || (years==y && mon==m && day==d)){
                            if (!quizStartTime.getText().toString().isEmpty()){
                                int hr = Calendar.getInstance().get(Calendar.HOUR);
                                int min = Calendar.getInstance().get(Calendar.MINUTE);
                                int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
                                int check1 = 1;
                                int check2 = 1;

                                if (am_pm == 1){
                                    hr = hr + 12;
                                }
                                if (day == d && mon == m && years == y){
                                    if (hr1 < hr){
                                        check1 = 0;
                                    } else {
                                        if (hr1 == hr){
                                            if (min1 < min){
                                                check1 = 0;
                                            }
                                        }
                                    }
                                }
                                if (!(day <= d && mon <= m && years <= y) || check1 == 1){
                                    if (!quizEndTime.getText().toString().isEmpty()){
                                        if (day == d && mon == m && years == y){
                                            if (hr2 < hr1){
                                                check2 = 0;
                                            } else {
                                                if (hr2 == hr1){
                                                    if (min2 < min1){
                                                        check2 = 0;
                                                    }
                                                }
                                            }
                                        }
                                        if (!(day <= d && mon <= m && years <= y) || check2 ==1){
                                            addQuizData();
                                        } else {
                                            Toast.makeText(getActivity(),"Enter valid Ending Quiz time",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(),"Enter Quiz Ending Time",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(),"Enter valid starting Quiz time",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(),"Enter starting Quiz time",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Please enter valid date",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Enter Quiz Date",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"Enter Quiz Name",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        dateSetListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                day = dayOfMonth;
                mon = month+1;
                years = year;
                String date =  day + "/" + mon + "/" + years;
                TextView setDate = dialog.findViewById(R.id.quiz_date);
                setDate.setText(date);
            }
        };

        setStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                hr1 = hour;
                                min1 = minute;
                                String time = hr1 + ":" + min1;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    TextView text_time = dialog.findViewById(R.id.start_quiz_time);
                                    text_time.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hr1,min1);
                timePickerDialog.show();
            }
        });

        setEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                hr2 = hour;
                                min2 = minute;
                                String time = hr2 + ":" + min2;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    TextView text_time = dialog.findViewById(R.id.end_quiz_time);
                                    text_time.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hr2,min2);
                timePickerDialog.show();
            }
        });

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        return view;
    }
    private void addQuizData() {
        Map<Object,Object> quiz =new HashMap<>();
        quiz.put("Quiz_Name",quizName.getText().toString());
        quiz.put("Day",String.valueOf(day));
        quiz.put("Month",String.valueOf(mon));
        quiz.put("Year",String.valueOf(years));
        quiz.put("hr1",String.valueOf(hr1));
        quiz.put("min1",String.valueOf(min1));
        quiz.put("hr2",String.valueOf(hr2));
        quiz.put("min2",String.valueOf(min2));

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .document(quizName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                Toast.makeText(getActivity(),"Quiz Name Already Exist",Toast.LENGTH_SHORT).show();
                            } else {
                                firebaseFirestore.collection("Class")
                                        .document(class_name)
                                        .collection("Quiz")
                                        .document(quizName.getText().toString())
                                        .set(quiz)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                            }
                                        });
                                remaingExamItemModelList.add(new RemaingExamItemModel(quizName.getText().toString(),
                                        String.valueOf(day)+"/"+String.valueOf(mon)+"/"+String.valueOf(years),
                                        String.valueOf(hr1)+":"+String.valueOf(min1)+" - "+String.valueOf(hr2)+":"+String.valueOf(min2)));
                                remaingExamAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}