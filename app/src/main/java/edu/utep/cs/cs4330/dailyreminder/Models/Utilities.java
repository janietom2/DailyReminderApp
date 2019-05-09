package edu.utep.cs.cs4330.dailyreminder.Models;

public class Utilities {

    public static int priorityConverter(String name){
        int p;

        switch (name){
            case "High":
                p = 0;
                break;
            case "Medium":
                p = 1;
                break;
            case "Low":
                p = 2;
                break;
                default:
                    p = 2;
                    break;
        }

        return p;
    }
}
