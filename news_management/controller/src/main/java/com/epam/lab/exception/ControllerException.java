package com.epam.lab.exception;


import javax.servlet.http.HttpServletResponse;

public abstract class ControllerException extends Exception {
    private String message;
    private int responseCod = HttpServletResponse.SC_BAD_GATEWAY;

    ControllerException() {
    }

    ControllerException(String message) {
        this.message = message;
    }
    ControllerException(String message, int responseCod) {
        this.message = message;
        this.responseCod = responseCod;
    }


    ControllerException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }
    ControllerException(String message, Throwable cause, int responseCod) {
        super(cause);
        this.message = message;
        this.responseCod = responseCod;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getResponseCod() {
        return responseCod;
    }

    public void setResponseCod(int responseCod) {
        this.responseCod = responseCod;
    }
}
