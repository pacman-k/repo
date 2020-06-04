package com.epam.lab.service;


import java.util.Locale;
import java.util.ResourceBundle;

public class ServiceMessager {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("servicemessages", Locale.ENGLISH);

    private ServiceMessager() {
    }

    public static String getMessage(String name){
        return bundle.getString(name);
    }
}
