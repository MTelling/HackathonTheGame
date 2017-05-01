package com.htg;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class ChallengeReader {
    public ChallengeReader() {}

    public ChallengeDescription getChallengeDescriptionFromPath(String path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.lines(FileSystems.getDefault().getPath(path)).forEachOrdered(stringBuilder::append);
        Gson gson = new Gson();
        String jsonFile = stringBuilder.toString();
        return gson.fromJson(jsonFile, ChallengeDescription.class);
    }
}
