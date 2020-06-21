package com.chelcov.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;
import java.util.Objects;

@JsonAutoDetect
public class LogRecord {

    private String username;
    private Date date;
    private String customMessage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogRecord logRecord = (LogRecord) o;
        return Objects.equals(username, logRecord.username) &&
                Objects.equals(date, logRecord.date) &&
                Objects.equals(customMessage, logRecord.customMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, date, customMessage);
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "username='" + username + '\'' +
                ", date=" + date +
                ", customMessage='" + customMessage + '\'' +
                '}';
    }
}
