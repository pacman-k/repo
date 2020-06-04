package com.epam.lab.exception;

import com.epam.lab.dto.UserDto;

public class UserControllerException extends ControllerException {
    private UserDto userDto;
    private String moreInfo;

    public UserControllerException() {

    }

    public UserControllerException(String message) {
        super(message);
    }

    public UserControllerException(UserDto userDto, String message) {
        super(message);
        this.userDto = userDto;
    }

    public UserControllerException(UserDto userDto, String message, String moreInfo) {
        super(message);
        this.userDto = userDto;
        this.moreInfo = moreInfo;
    }
    public UserControllerException(UserDto userDto, String message, int responseCod) {
        super(message, responseCod);
        this.userDto = userDto;
    }

    public UserControllerException(UserDto userDto, String message, String moreInfo, int responseCod) {
        super(message, responseCod);
        this.userDto = userDto;
        this.moreInfo = moreInfo;
    }

    public UserControllerException(UserDto userDto, String message, String moreInfo, Throwable cause) {
        super(message, cause);
        this.userDto = userDto;
        this.moreInfo = moreInfo;
    }

    public UserControllerException(UserDto userDto, String message, String moreInfo, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
        this.userDto = userDto;
        this.moreInfo = moreInfo;
    }

    public UserControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserControllerException(String message, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "; User{"
                + userToString() + "}; more info{"
                + moreInfo + "}";
    }

    private String userToString() {
        return (userDto == null ? null : userDto.getName() + ", " + userDto.getSurname()
                + ", " + userDto.getLogin());
    }
}
