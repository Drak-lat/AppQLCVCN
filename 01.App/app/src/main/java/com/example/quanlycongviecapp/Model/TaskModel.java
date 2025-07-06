package com.example.quanlycongviecapp.Model;

public class TaskModel {

    private int id;
    private String title;
    private String description;
    private String dueDate;     // Ví dụ: "2025-07-31"
    private int priority;       // Ví dụ: 1 - thấp, 2 - trung bình, 3 - cao
    private boolean completed;
    private int planId;         // Khóa ngoại liên kết Plan

    // Constructor không tham số (bắt buộc cho Retrofit/Gson)
    public TaskModel() {
    }

    // Constructor đầy đủ (tuỳ chọn)
    public TaskModel(int id, String title, String description, String dueDate,
                     int priority, boolean completed, int planId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
        this.planId = planId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPlanId() {
        return planId;
    }
    public void setPlanId(int planId) {
        this.planId = planId;
    }
}
