package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainThird {
    public static void main(String[] args) {
        readWriteFile();
    }

    private static void readWriteFile() {
        List<String> listString = new ArrayList<>();
        String in = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
        String out = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(in))) {
            String s;
            while ((s=br.readLine())!=null) {
                listString.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
//
//        }
        System.out.println(listString);
    }
}
