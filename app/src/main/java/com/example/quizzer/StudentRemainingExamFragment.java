package com.example.quizzer;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.quizzer.StudentQuizListActivity.class_name;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentRemainingExamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentRemainingExamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentRemainingExamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentRemainingExamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentRemainingExamFragment newInstance(String param1, String param2) {
        StudentRemainingExamFragment fragment = new StudentRemainingExamFragment();
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

    private RecyclerView studentRemaingExamRecycleView;
    private List<CompleteExamItemModel> studentRemaingExamItemModelList;
    private StudentRemaingExamAdaper studentRemaingExamAdapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_remaining_exam, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        studentRemaingExamRecycleView = view.findViewById(R.id.student_remaing_exam_recycleView);
        studentRemaingExamItemModelList = new ArrayList<>();
        studentRemaingExamAdapter = new StudentRemaingExamAdaper(studentRemaingExamItemModelList,class_name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        studentRemaingExamRecycleView.setLayoutManager(layoutManager);
        studentRemaingExamRecycleView.setAdapter(studentRemaingExamAdapter);

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
                                    studentRemaingExamItemModelList.add(new CompleteExamItemModel(name,day+"/"+month+"/"+year,hr1+":"+min1+" - "+hr2+":"+min2));
                                }
                            }
                            studentRemaingExamAdapter.notifyDataSetChanged();
                        }
                    }
                });

        /*studentRemaingExamItemModelList.add(new CompleteExamItemModel("Name","Date", "Time"));
        studentRemaingExamItemModelList.add(new CompleteExamItemModel("Name","Date", "Time"));
        studentRemaingExamItemModelList.add(new CompleteExamItemModel("Name","Date", "Time"));
        studentRemaingExamItemModelList.add(new CompleteExamItemModel("Name","Date", "Time"));
        studentRemaingExamItemModelList.add(new CompleteExamItemModel("Name","Date", "Time"));

        studentRemaingExamAdapter.notifyDataSetChanged();*/
        return view;
    }
}