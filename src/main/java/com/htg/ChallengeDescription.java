package com.htg;

import java.util.List;

/**
 * Created by Morten on 22/04/2017.
 */
public class ChallengeDescription {

    private String name, description, initialCode;
    private List<Test> tests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }
}

class Test {
    private String[] arguments;
    private Object[] expectedReturns;

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public Object[] getExpectedReturns() {
        return expectedReturns;
    }

    public void setExpectedReturns(Object[] expectedReturns) {
        this.expectedReturns = expectedReturns;
    }
}

