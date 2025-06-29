package com.example.quanlycongviecapp;

public class Task {
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private int priority;
    private boolean completed;


    public Task(String title, String description, String dueDate, int priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(int id, String title, String description, String dueDate, int priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
