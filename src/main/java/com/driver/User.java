package com.driver;

import java.util.Objects;

public class User {
    private String name;
    private String mobile;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(name, user.name) && Objects.equals(mobile, user.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mobile);
    }
}
