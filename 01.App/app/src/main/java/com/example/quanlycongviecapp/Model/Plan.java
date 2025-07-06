package com.example.quanlycongviecapp.Model;

public class Plan {
    private int id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;

    // Constructor không tham số (bắt buộc có khi dùng Retrofit, Gson)
    public Plan() {
    }

    // Constructor đầy đủ nếu cần (không bắt buộc)
    public Plan(int id, String name, String description, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter - Setter cho tất cả trường

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
