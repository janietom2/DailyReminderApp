package edu.utep.cs.cs4330.dailyreminder.Models;

import java.util.Calendar;
import java.util.Date;

public class Task {

    private String title;
    private String description;
    private Date startDate;
    private File[] files;
    private Date deadLine;
    private String id;
    private Boolean finished;


    public Task(String title, int priority, String id){
        Date currentTime = Calendar.getInstance().getTime();

        this.title = title;
        this.description = "No description given yet";
        this.deadLine = currentTime;
        this.priority = priority;
        this.startDate = currentTime;
        this.id = id;
        this.finished = false;
    }

    public Task(String title, String description,Date startDate, Date deadLine, int priority, String id) {
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.priority = priority;
        this.startDate = startDate;
        this.id = id;
        this.finished = false;
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

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
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

    public Boolean getFinished(){
        return this.finished;
    }

    public void setFinished(Boolean status) {
        this.finished = status;
    }
}
