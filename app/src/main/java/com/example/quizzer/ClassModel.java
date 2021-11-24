package com.example.quizzer;

public class ClassModel {

    private String class_ch;
    private String class_name;

    public ClassModel(String class_ch, String class_name) {
        this.class_ch = class_ch;
        this.class_name = class_name;
    }

    public String getClass_ch() {
        return class_ch;
    }

    public void setClass_ch(String class_ch) {
        this.class_ch = class_ch;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
