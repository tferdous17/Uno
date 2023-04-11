package com.example.uno;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameSettings {
    // This method will read t
    public void readSampleFile(String fileName) {
        ArrayList<String> linesRead = new ArrayList<>();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                linesRead.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
