package com.epam.lab.controller;


import java.util.Locale;
import java.util.ResourceBundle;

public class ControllerMessager {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("controllermessages", Locale.ENGLISH);

    private ControllerMessager() {
    }

    public static String getMessage(String name){
            return bundle.getString(name);
    }

}
