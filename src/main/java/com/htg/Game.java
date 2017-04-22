package com.htg;

import java.util.ArrayList;

public class Game {

    private String description;
    private ArrayList<Problem> problems = new ArrayList<>();

    public Game (String description) {
        this.description = description;
    }

    public void addProblem (String input, String output) {
        problems.add( new Problem(input, output) );
    }

    public Game copy () {
        return new Game(getDescription());
    }

    public String getDescription () {
        return description;
    }
}

class Problem {
    private String input, output;

    Problem(String input, String output) {
        this.input = input;
        this.output = output;
    }

    boolean correctSolution (String yourResult ){
        return yourResult.equals(output);
    }

    public String getInput() {
        return input;
    }
}