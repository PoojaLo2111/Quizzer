package com.example.quizzer;

import java.security.PrivateKey;

public class RemaingExamItemModel {
    private String remaingExamName;
    private String remaingExamDate;
    private String remaingExamTime;

    public RemaingExamItemModel(String remaingExamName, String remaingExamDate, String remaingExamTime) {
        this.remaingExamName = remaingExamName;
        this.remaingExamDate = remaingExamDate;
        this.remaingExamTime = remaingExamTime;
    }

    public String getRemaingExamName() {
        return remaingExamName;
    }

    public void setRemaingExamName(String remaingExamName) {
        this.remaingExamName = remaingExamName;
    }

    public String getRemaingExamDate() {
        return remaingExamDate;
    }

    public void setRemaingExamDate(String remaingExamDate) {
        this.remaingExamDate = remaingExamDate;
    }

    public String getRemaingExamTime() {
        return remaingExamTime;
    }

    public void setRemaingExamTime(String remaingExamTime) {
        this.remaingExamTime = remaingExamTime;
    }
}
