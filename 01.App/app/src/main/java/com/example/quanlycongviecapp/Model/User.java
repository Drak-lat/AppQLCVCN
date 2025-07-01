package com.example.quanlycongviecapp.Model;
import java.io.Serializable;

public class User  {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String birthdate;
    private String address;
    private String avatarUrl;

    // Constructor đơn giản dùng cho Đăng ký
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.username = email; // Username trùng với email
    }

    // Constructor đầy đủ (nếu dùng khi cập nhật profile)
    public User(String username, String password, String fullName, String email, String phone, String birthdate, String address, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int userId) { this.id = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }


}
