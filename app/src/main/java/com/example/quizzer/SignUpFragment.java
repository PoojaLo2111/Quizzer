package com.example.quizzer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.quizzer.SignUpFragment newInstance(String param1, String param2) {
        com.example.quizzer.SignUpFragment fragment = new com.example.quizzer.SignUpFragment();
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

    private TextView alreadyhaveaccount;
    private FrameLayout parentFramelayout;

    private EditText username;
    private EditText email;
    private RadioGroup radioGroup;
    private EditText pass;
    private EditText confpass;
    private String emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    private Button signup;
    private RadioButton radioButton;

    int id;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyhaveaccount = view.findViewById(R.id.already_have_account);
        parentFramelayout = getActivity().findViewById(R.id.framelayout);

        username = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.signup_email);
        radioGroup = view.findViewById(R.id.radioGroup);
        pass = view.findViewById(R.id.password_signup);
        confpass = view.findViewById(R.id.conform_pass);

        signup = view.findViewById(R.id.signup_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfragment(new SignInFragment());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(id);
                checkData();
            }
        });
    }

    private void checkData() {
        if (!username.getText().toString().isEmpty()){
            if(email.getText().toString().matches(emailpattern)){
                if(!pass.getText().toString().isEmpty() && pass.getText().toString().length()>=8){
                    if(pass.getText().toString().equals(confpass.getText().toString())){
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            Map<Object,String> userdata =new HashMap<>();
                                            userdata.put("Username",username.getText().toString());
                                            userdata.put("Email",email.getText().toString());
                                            firebaseFirestore.collection(radioButton.getText().toString())
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .set(userdata)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent mainIntent = new Intent(getActivity(),MainActivity2.class);
                                                            startActivity(mainIntent);
                                                            getActivity().finish();
                                                        }
                                                    });
                                        }
                                        else{
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        confpass.setError("Password doesn't match");
                    }
                } else {
                    pass.setError("Password is invalid");
                }
            } else {
                email.setError("Invalid email");
            }
        } else {
            username.setError("Write Username");
        }

    }

//    private void checkData() {
//        if (!TextUtils.isEmpty(username.getText())){
//            if (!TextUtils.isEmpty(email.getText())){
//                if (!TextUtils.isEmpty(pass.getText())){
//                    if (!TextUtils.isEmpty(confpass.getText())){
//
//                    } else {
//
//                    }
//                } else {
//
//                }
//            } else {
//
//            }
//        } else {
//
//        }
//    }

    private void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFramelayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}