package com.htg;

public class Problem {
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
