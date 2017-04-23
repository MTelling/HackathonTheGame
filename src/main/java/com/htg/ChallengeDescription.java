package com.htg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Morten on 22/04/2017.
 */
public class ChallengeDescription {

    private String name, description, filename;
    private List<Test> tests;
    private Map<String, String> initialCode = new HashMap<>();

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
        return initialCode.get("java");
    }

    public void setInitialCode(String initialCode) {
        this.initialCode.put("java", initialCode);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

class Test {
    private String[] arguments;
    private Object expectedReturn;

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public Object getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(Object expectedReturn) {
        this.expectedReturn = expectedReturn;
    }
}


