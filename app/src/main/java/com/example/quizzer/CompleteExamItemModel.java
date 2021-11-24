package com.example.quizzer;

public class CompleteExamItemModel {
    private String completeExamName;
    private String completeExamDate;
    private String completeExamTime;

    public CompleteExamItemModel(String completeExamName, String completeExamDate, String completeExamTime) {
        this.completeExamName = completeExamName;
        this.completeExamDate = completeExamDate;
        this.completeExamTime = completeExamTime;
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
}
