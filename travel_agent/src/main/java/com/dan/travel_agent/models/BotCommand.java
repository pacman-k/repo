package com.dan.travel_agent.models;

import java.util.Objects;

public class BotCommand {
    private static final String DEFAULT_VALUE = "Ooops!!! Sorry, no such command...";

    private String command;
    private String value;

    public BotCommand() {
    }

    public static String getDefaultValue(){
        return DEFAULT_VALUE;
    }

    public boolean isThatCommand(String command){
        return this.command.equalsIgnoreCase(command);
    }



    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotCommand that = (BotCommand) o;
        return Objects.equals(command, that.command) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, value);
    }

    @Override
    public String toString() {
        return "BotCommand{" +
                "command='" + command + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
