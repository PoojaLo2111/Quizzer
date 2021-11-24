package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.ExtendedSSLSession;

import jxl.Workbook;

import static android.content.ContentValues.TAG;
import static com.example.quizzer.QuizListActivity.class_name;

public class QuestionListActivity extends AppCompatActivity {

    private RecyclerView questionListRecycleView;
    private List<QuestionListItemModel> questionListItemModelList;
    private QustionListAdapter qustionListAdapter;
    private FirebaseFirestore firebaseFirestore;
    private Button addQuestion, addExcel;
    private String Class_name, Quiz_name;
    private List<QuestionModelClass> que_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        questionListRecycleView = findViewById(R.id.questions_recycleView);
        addQuestion = findViewById(R.id.add_question_button);
        addExcel = findViewById(R.id.add_excel_button);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Class_name = getIntent().getStringExtra("class_name");
        Quiz_name = getIntent().getStringExtra("Quiz_name");

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionListActivity.this,AddQuestionActivity.class);
                intent.putExtra("class_name", Class_name);
                intent.putExtra("Quiz_name",Quiz_name);
                startActivity(intent);
            }
        });

        addExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(QuestionListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectFile();
                } else {
                    ActivityCompat.requestPermissions(QuestionListActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }
            }
        });

        questionListItemModelList = new ArrayList<>();
        qustionListAdapter = new QustionListAdapter(questionListItemModelList,Class_name,Quiz_name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        questionListRecycleView.setLayoutManager(layoutManager);
        questionListRecycleView.setAdapter(qustionListAdapter);

        firebaseFirestore.collection("Class")
                .document(class_name)
                .collection("Quiz")
                .document(Quiz_name)
                .collection("Questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                String que = documentSnapshot.getString("Question");
                                String ans = documentSnapshot.getString("CorrectAns");
                                questionListItemModelList.add(new QuestionListItemModel(que,ans));
                            }
                            qustionListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectFile();
            } else {
                Toast.makeText(QuestionListActivity.this,"Please Grant Permissions!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Select File"),102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102){
            if (resultCode == RESULT_OK){
                String filepath = data.getData().getPath();
                if (filepath.endsWith(".xlsx")){
                    readFile(filepath);

                    que_list = new ArrayList<>();
                    que_list.add(new QuestionModelClass("What is Capital of india?","Delhi","Ahmedabad","Mumbai","surat","Delhi"));
                    que_list.add(new QuestionModelClass("What does your heart pump?","Blood","Hair","Air","Skin","Blood"));
                    que_list.add(new QuestionModelClass("What is H2O?","salt","Blood","Water","None","Water"));
                    que_list.add(new QuestionModelClass("What is Symbol of Lead?","Na","Pb","He","Li","Pb"));

                    for (int i = 0 ; i<que_list.size();i++){
                        String que = que_list.get(i).getQuestion();
                        String a = que_list.get(i).getOptionA();
                        String b = que_list.get(i).getOptionB();
                        String c = que_list.get(i).getOptionC();
                        String d = que_list.get(i).getOptionD();
                        String correct = que_list.get(i).getCorrectAns();

                        Map<Object,Object> questionMap =new HashMap<>();
                        questionMap.put("Question",que);
                        questionMap.put("CorrectAns",correct);
                        questionMap.put("OptionA",a);
                        questionMap.put("OptionB",b);
                        questionMap.put("OptionC",c);
                        questionMap.put("OptionD",d);

                        firebaseFirestore.collection("Class")
                                .document(class_name)
                                .collection("Quiz")
                                .document(Quiz_name)
                                .collection("Questions")
                                .add(questionMap)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(QuestionListActivity.this,"File Selected",Toast.LENGTH_SHORT).show();
                                    }
                                });


                        questionListItemModelList.add(new QuestionListItemModel(que,correct));

                        qustionListAdapter.notifyDataSetChanged();
                    }

//                    Map<Object,Object> questionMap =new HashMap<>();
//                    questionMap.put("Question",question.getText().toString());
//                    questionMap.put("CorrectAns",correctAns.getText().toString());
//                    questionMap.put("OptionA",optionA.getText().toString());
//                    questionMap.put("OptionB",optionB.getText().toString());
//                    questionMap.put("OptionC",optionC.getText().toString());
//                    questionMap.put("OptionD",optionD.getText().toString());
//
//                    firebaseFirestore.collection("Class")
//                            .document(class_name)
//                            .collection("Quiz")
//                            .document(Quiz_name)
//                            .collection("Questions")
//                            .add(questionMap)
//                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentReference> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(AddQuestionActivity.this,"Add",Toast.LENGTH_SHORT).show();
//                                        question.setText("");
//                                        correctAns.setText("");
//                                        optionA.setText("");
//                                        optionB.setText("");
//                                        optionC.setText("");
//                                        optionD.setText("");
//                                        finish();
//                                    }
//                                }
//                            });
//                    readFile(data.getData());
//                    Toast.makeText(QuestionListActivity.this,"File Selected",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionListActivity.this,"Please choose an Excel File",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void readFile(String filepath/*Uri fileUri*/){

//        File file1 = new File(filepath);
//        String fileName = file1.getName();

//        StringBuilder stringBuilder = new StringBuilder();
//        try (InputStream inputStream =
//                     getContentResolver().openInputStream(fileUri);
//             BufferedReader reader = new BufferedReader(
//                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                Toast.makeText(QuestionListActivity.this,line,Toast.LENGTH_SHORT).show();;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();


        FileInputStream fis= null;
        try {
            fis = new FileInputStream(new File(filepath));
            Toast.makeText(QuestionListActivity.this,"1",Toast.LENGTH_SHORT).show();
            HSSFWorkbook wb= null;
            wb = new HSSFWorkbook(fis);
            HSSFSheet sheet=wb.getSheetAt(0);
            FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();
            for(Row row: sheet)
            {
                for(Cell cell: row)
                {
                    switch(formulaEvaluator.evaluateInCell(cell).getCellType())
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue()+ "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue()+ "\t\t");
                            break;
                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            InputStream inputStream = new FileInputStream(file);
//            Toast.makeText(QuestionListActivity.this,"1",Toast.LENGTH_SHORT).show();
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            int rows = sheet.getPhysicalNumberOfRows();
//
//            if (rows > 0){
//                for (int r = 0 ; r < rows ; r++){
//                    Row row = sheet.getRow(r);
//                    Toast.makeText(QuestionListActivity.this,"3",Toast.LENGTH_SHORT).show();
//
//                    if (row.getPhysicalNumberOfCells() == 6){
//                        String question = getCellData(row,0,formulaEvaluator);
//                        String a = getCellData(row,1,formulaEvaluator);
//                        String b = getCellData(row,2,formulaEvaluator);
//                        String c = getCellData(row,3,formulaEvaluator);
//                        String d = getCellData(row,4,formulaEvaluator);
//                        String correctAns = getCellData(row,5,formulaEvaluator);
//                        Toast.makeText(QuestionListActivity.this,"4",Toast.LENGTH_SHORT).show();
//
//                        if (correctAns == a || correctAns == b || correctAns == c || correctAns == d){
//                            HashMap<String,Object> questionMap = new HashMap<>();
//                            questionMap.put("Question",question);
//                            questionMap.put("OptionA",a);
//                            questionMap.put("OptionB",b);
//                            questionMap.put("OptionC",c);
//                            questionMap.put("OptionD",d);
//                            questionMap.put("Correct",correctAns);
//                            Toast.makeText(QuestionListActivity.this,"5",Toast.LENGTH_SHORT).show();
//
//                            firebaseFirestore.collection("Class")
//                                    .document(Class_name)
//                                    .collection("Quiz")
//                                    .document(Quiz_name)
//                                    .collection("Questions")
//                                    .add(questionMap)
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                                            Toast.makeText(QuestionListActivity.this,"6",Toast.LENGTH_SHORT).show();
//
//                                            questionListItemModelList.add(new QuestionListItemModel(question,correctAns));
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(QuestionListActivity.this,"Row no "+(r+1)+" has no incorect option",Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(QuestionListActivity.this,"Row no "+(r+1)+" has incorect data",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else {
//                Toast.makeText(QuestionListActivity.this,"File is empty",Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private String getCellData(Row row, int cellPosition, FormulaEvaluator formulaEvaluator) {
        String value = "";
        Cell cell = row.getCell(cellPosition);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BOOLEAN:
                return value + cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return value + cell.getNumericCellValue();

            case Cell.CELL_TYPE_STRING:
                return value + cell.getStringCellValue();

            default:
                return value;
        }
    }
}