package com.htg;

import java.util.ArrayList;

public class Game {

    private String description;
    private ArrayList<Problem> problems = new ArrayList<>();

    public Game (String description) {
        this.description = description;
    }

    synchronized public void addProblem (String input, String output) {
        problems.add( new Problem(input, output) );
    }

    synchronized public boolean[] testResults( ArrayList<String> outputs ) {
        boolean[] results = new boolean[outputs.size()];
        for(int i = 0; i < problems.size(); i++){
            if(outputs.size() <= i) break; // Not all problems were run?
            results[i] = problems.get(i).correctSolution(outputs.get(i));
        }
        return results;
    }

    synchronized public String[] getChallengeInputs () {
        String[] inputs = new String[problems.size()];
        for(int i = 0; i < inputs.length; i++)
            inputs[i] = problems.get(i).getInput();
        return inputs;
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

    Problem (String input, String output) {
        this.input = input;
        this.output = output;
    }

    boolean correctSolution (String yourResult ){
        return yourResult.equals(output);
    }

    public String getInput () {
        return input;
    }
}