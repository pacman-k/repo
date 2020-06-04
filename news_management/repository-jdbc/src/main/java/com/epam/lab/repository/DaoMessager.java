package com.epam.lab.repository;


import java.util.Locale;
import java.util.ResourceBundle;

public class DaoMessager {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("daomessages", Locale.ENGLISH);

    private DaoMessager() {
    }

    public static String getMessage(String name) {
        return bundle.getString(name);
    }
}
