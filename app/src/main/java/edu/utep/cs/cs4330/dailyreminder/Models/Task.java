package edu.utep.cs.cs4330.dailyreminder.Models;

import java.util.Calendar;
import java.util.Date;

public class Task {

    private String title;
    private String description;
    private String startDate;
    private File[] files;
    private String deadLine;
    private String id;
    private int finished;


    public Task(String title, int priority, String id){
        Date currentTime = Calendar.getInstance().getTime();

        this.title = title;
        this.description = "No description given yet";
        this.deadLine = currentTime.toString();
        this.priority = priority;
        this.startDate = currentTime.toString();
        this.id = id;
        this.finished = 0;
    }

    public Task(String title, String description, String startDate, String deadLine, int priority, String id, int finished) {
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.priority = priority;
        this.startDate = startDate;
        this.id = id;
        this.finished = finished;
    }

    private int priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartDate(){
        return this.startDate;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getId(){
        return this.id;
    }

    public int getFinished(){
        return this.finished;
    }

    public void setFinished(int status) {
        this.finished = status;
    }
}
