package com.example.galeford.models;

import java.util.List;

public class Users {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GENDER = "gender";
    public static final String PHONE = "phone";
    public static final String FAV_PRODUCTS = "favProducts";

    private String username;
    private String email;
    private String password;
    private String userAvatar;
    private String phone;
    private String gender;
    private List<String> favProducts;
    private String userId;

    public Users() {}

    public Users(String uname, String email, String password, String phone, List<String> fp, String userAvatar) {
        this.username = uname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.favProducts = fp;
        this.userAvatar = userAvatar;
    }

    public String _getUserId() {
        return userId;
    }

    public void _setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFavProducts() {
        return favProducts;
    }

    public void setFavProducts(List<String> favProducts) {
        this.favProducts = favProducts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
