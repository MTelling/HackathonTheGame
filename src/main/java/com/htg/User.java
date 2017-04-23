package com.htg;

public class User {

    private String name;

    private int score = 0;

    User(String name) {
        this.name = name;
    }

    public String getName(){ return name; }

    public void addScore(int i){
        score += i;
    }

    public int getScore() {
        return score;
    }
}
