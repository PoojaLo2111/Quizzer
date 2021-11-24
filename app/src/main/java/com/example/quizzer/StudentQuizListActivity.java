package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class StudentQuizListActivity extends AppCompatActivity {
    public static String class_name;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_list);

        class_name =getIntent().getStringExtra("Class");
        tabLayout = (TabLayout) findViewById(R.id.student_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.student_viewPager);

        StudentTabPageAdapter tabPageAdapter = new StudentTabPageAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(tabPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(StudentQuizListActivity.this,MainActivity2.class);
        startActivity(mainIntent);
        finish();
    }
}