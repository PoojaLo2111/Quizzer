package com.example.quizzer;

public class StudentComapleteExamItemModel {
    private String completeExamName;
    private String completeExamDate;
    private String completeExamTime;
    private String completeExamMarks;

    public StudentComapleteExamItemModel(String completeExamName, String completeExamDate, String completeExamTime, String completeExamMarks) {
        this.completeExamName = completeExamName;
        this.completeExamDate = completeExamDate;
        this.completeExamTime = completeExamTime;
        this.completeExamMarks = completeExamMarks;
    }

    public String getCompleteExamName() {
        return completeExamName;
    }

    public void setCompleteExamName(String completeExamName) {
        this.completeExamName = completeExamName;
    }

    public String getCompleteExamDate() {
        return completeExamDate;
    }

    public void setCompleteExamDate(String completeExamDate) {
        this.completeExamDate = completeExamDate;
    }

    public String getCompleteExamTime() {
        return completeExamTime;
    }

    public void setCompleteExamTime(String completeExamTime) {
        this.completeExamTime = completeExamTime;
    }

    public String getCompleteExamMarks() {
        return completeExamMarks;
    }

    public void setCompleteExamMarks(String completeExamMarks) {
        this.completeExamMarks = completeExamMarks;
    }
}
