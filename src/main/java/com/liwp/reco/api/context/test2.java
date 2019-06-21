package com.liwp.reco.api.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class test2 {
    public static void main(String args[]) throws Exception{
        String path = "";
        BufferedReader reader = new BufferedReader(new FileReader(path + ""));
        FileWriter writer = new FileWriter(path + "");
        String line = reader.readLine();
        while(line != null) {
            System.out.println(line);
            line = reader.readLine();
        }

    }
}
