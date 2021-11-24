package com.example.quizzer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherFragment extends Fragment implements ClassAdapter.OnNoteLlistener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherFragment newInstance(String param1, String param2) {
        TeacherFragment fragment = new TeacherFragment();
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

    private RecyclerView classRecycleView;
    private List<ClassModel> classModelList;
    private ClassAdapter classAdapter;
    private Dialog dialog;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        classRecycleView = view.findViewById(R.id.Class_list_recycleview);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this.getContext());

        dialog.setContentView(R.layout.create_new_class);
        dialog.setTitle("Create Class");
        dialog.setCancelable(true);

        EditText newClass = dialog.findViewById(R.id.new_quiz_name);
        Button create = dialog.findViewById(R.id.create_quiz);
        Button cancle = dialog.findViewById(R.id.cancel_quiz);
        // Show the Alert Dialog box
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> userdata =new HashMap<>();
                userdata.put("Teacher",firebaseAuth.getCurrentUser().getUid());
                userdata.put("Class Name",newClass.getText().toString());

                firebaseFirestore.collection("Class")
                        .document(newClass.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(),"Class Already Exist",Toast.LENGTH_SHORT).show();
                                    } else {
                                        firebaseFirestore.collection("Class")
                                                .document(newClass.getText().toString())
                                                .set(userdata)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        callActivity(newClass.getText().toString());
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        classModelList = new ArrayList<>();
        classAdapter = new ClassAdapter(classModelList, /*TeacherFragment.this*/this.getContext(),this);
        classRecycleView.setLayoutManager(new GridLayoutManager(getContext(),3));
        classRecycleView.setAdapter(classAdapter);
        classModelList.add(new ClassModel("+","Add class "));

        firebaseFirestore.collection("Class")
                .whereEqualTo("Teacher",firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String class_name,class_ch;
                                class_name = documentSnapshot.getString("Class Name");
                                class_ch = String.valueOf(class_name.charAt(0));
                                classModelList.add(new ClassModel(class_ch,class_name));
                            }
                            classAdapter.notifyDataSetChanged();
                        }
                    }
                });

        return view;
    }

    @Override
    public void onNoteclick(int position) {

        if (position == 0){
            dialog.show();
        } else {
            callActivity(classModelList.get(position).getClass_name());
        }
    }

    @Override
    public void onNoteLongClick(int position) {
        if (position != 0){

            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(getContext());
            builder.setMessage("Do you want to delete this class ?");
            builder.setTitle("Delete Class !");
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            String class_name = classModelList.get(position).getClass_name();
                            firebaseFirestore.collection("Class")
                            .document(class_name)
                            .collection("Students")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                                            firebaseFirestore.collection("Student")
                                                .document(documentSnapshot.getString("std_id"))
                                                .collection("Class")
                                                .document(class_name).delete();
                                        }
                                    }
                                }
                            });
                            firebaseFirestore.collection("Class").document(class_name).delete();

                            classModelList.remove(position);
                            classAdapter.notifyDataSetChanged();
                        }
                    });
            builder.setNegativeButton(
                    "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void callActivity(String c_name){
        Intent mainIntent = new Intent(getActivity(),QuizListActivity.class);
        mainIntent.putExtra("Class",c_name);
        startActivity(mainIntent);
        getActivity().finish();
    }
}