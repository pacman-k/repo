package com.chelcov.models;

import java.util.Objects;

public class GroupingModel {
    private String username;
    private String date;

    GroupingModel(String username, String date) {
        this.username = username;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupingModel that = (GroupingModel) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, date);
    }

    @Override
    public String toString() {
        return "GroupingModel{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
