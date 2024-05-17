package com.example.myapp.models;

public class Word {
    private String english;
    private String vietnamese;

    public Word() {
    }

    public Word(String english, String vietnamese) {
        this.english = english;
        this.vietnamese = vietnamese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", vietnamese='" + vietnamese + '\'' +
                '}';
    }
}
