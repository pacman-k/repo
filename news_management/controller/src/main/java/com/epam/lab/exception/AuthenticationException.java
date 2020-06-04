package com.epam.lab.exception;

import com.epam.lab.model.AuthenticationRequest;

public class AuthenticationException extends ControllerException {
    private AuthenticationRequest request;
    private String moreInfo;

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(AuthenticationRequest request, String message) {
        super(message);
        this.request = request;
    }
    public AuthenticationException(AuthenticationRequest request, String message, int responseCod) {
        super(message, responseCod);
        this.request = request;
    }
    public AuthenticationException(AuthenticationRequest request, String message, String moreInfo) {
        super(message);
        this.request = request;
        this.moreInfo = moreInfo;
    }
    public AuthenticationException(AuthenticationRequest request, String message, String moreInfo, int responseCod) {
        super(message, responseCod);
        this.request = request;
        this.moreInfo = moreInfo;
    }
    public AuthenticationException(AuthenticationRequest request, String message, String moreInfo, Throwable cause) {
        super(message, cause);
        this.request = request;
        this.moreInfo = moreInfo;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "; request{"
                + requestToString() + "}; more info{" + moreInfo + "}";
    }

    private String requestToString(){
        return (request == null ? null : request.getLogin() + ", " + request.getPassword());
    }


}
