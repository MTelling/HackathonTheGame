package com.htg;

import java.util.ArrayList;

public class Challenge {

    private String description, internalName, externalName;
    private ArrayList<Problem> problems = new ArrayList<>();

    public Challenge(String internalName, String externalName, String description) {
        this.internalName = internalName;
        this.externalName = externalName;
        this.description = description;
    }

    public void addProblem (String input, String output) {
        problems.add( new Problem(input, output) );
    }

    synchronized public boolean[] testResults(ArrayList<String> outputs) {
        boolean[] results = new boolean[outputs.size()];
        for (int i = 0; i < problems.size(); i++) {
            if (outputs.size() <= i) break; // Not all problems were run?
            results[i] = problems.get(i).correctSolution(outputs.get(i));
        }
        return results;
    }

    synchronized public String[] getChallengeInputs() {
        String[] inputs = new String[problems.size()];
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = problems.get(i).getInput();
        return inputs;
    }

    public Challenge copy() {
        return new Challenge(getInternalName(), getExternalName(), getDescription());
    }

    public String getDescription() {
        return description;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }



}

