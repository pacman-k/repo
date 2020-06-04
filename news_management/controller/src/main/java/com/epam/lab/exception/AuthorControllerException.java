package com.epam.lab.exception;

import com.epam.lab.dto.AuthorDto;

public class AuthorControllerException extends ControllerException {
    private AuthorDto authorDto;
    private String moreInfo;

    public AuthorControllerException() {
    }

    public AuthorControllerException(String message) {
        super(message);
    }

    public AuthorControllerException(AuthorDto authorDto, String message) {
        super(message);
        this.authorDto = authorDto;
    }
    public AuthorControllerException(AuthorDto authorDto, String message, int responseCod) {
        super(message, responseCod);
        this.authorDto = authorDto;
    }
    public AuthorControllerException(AuthorDto authorDto, String message, String moreInfo) {
        super(message);
        this.authorDto = authorDto;
        this.moreInfo = moreInfo;
    }
    public AuthorControllerException(AuthorDto authorDto, String message, String moreInfo, int responseCod) {
        super(message, responseCod);
        this.authorDto = authorDto;
        this.moreInfo = moreInfo;
    }
    public AuthorControllerException(AuthorDto authorDto, String message, String moreInfo, Throwable cause) {
        super(message, cause);
        this.authorDto = authorDto;
        this.moreInfo = moreInfo;
    }

    public AuthorControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorControllerException(String message, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "; Author{"
      + authorToString() + "}; more info{" + moreInfo + "}";
    }

    private String authorToString(){
        return (authorDto == null ? null : authorDto.getName() + ", " + authorDto.getSurname());
    }

}
