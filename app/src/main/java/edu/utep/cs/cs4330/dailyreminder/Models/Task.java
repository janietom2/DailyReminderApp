package edu.utep.cs.cs4330.dailyreminder.Models;

import java.util.Date;

public class Task {

    private String title;
    private String description;
    private Date startDate;
    private File[] files;
    private Date deadLine;
    private String id;

    public Task(String title, String description,Date startDate, Date deadLine, int priority, String id) {
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.priority = priority;
        this.startDate = startDate;
        this.id = id;
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
}
