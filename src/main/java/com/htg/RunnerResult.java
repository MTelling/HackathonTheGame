package com.htg;

import java.util.List;

/**
 * Created by Morten on 22/04/2017.
 */
public class RunnerResult {
    private boolean success;
    private int passedTests;


    public RunnerResult() {};

    private List<String> errors;
    private List<String> runtimeErrors;

    public List<String> getRuntimeErrors() {
        return runtimeErrors;
    }

    public void setRuntimeErrors(List<String> runtimeErrors) {
        this.runtimeErrors = runtimeErrors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
