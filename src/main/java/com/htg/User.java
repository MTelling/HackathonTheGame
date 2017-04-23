package com.htg;

public class User {

    private String name;
    private String lowName;

    private int score = 0;

    User(String name) {
        this.name = name;
        this.lowName = name.toLowerCase();
    }

    public String getName(){ return name; }

    public void addScore(int i){
        score += i;
    }

    public int getScore() {
        return score;
    }

    public String getLowName() {
        return lowName;
    }
}
