package com.epam.lab.exception;


import com.epam.lab.dto.TagDto;


public class TagControllerException extends ControllerException {
    private TagDto tagDto;
    private String moreInfo;

    public TagControllerException() {
    }

    public TagControllerException(String message) {
        super(message);
    }

    public TagControllerException(TagDto tagDto, String message) {
        super(message);
        this.tagDto = tagDto;
    }
    public TagControllerException(TagDto tagDto, String message, String moreInfo) {
        super(message);
        this.tagDto = tagDto;
        this.moreInfo = moreInfo;
    }
    public TagControllerException(TagDto tagDto, String message, String moreInfo, int responseCod) {
        super(message, responseCod);
        this.tagDto = tagDto;
        this.moreInfo = moreInfo;
    }

    public TagControllerException(TagDto tagDto, String message, String moreInfo, Throwable cause) {
        super(message, cause);
        this.tagDto = tagDto;
        this.moreInfo = moreInfo;
    }

    public TagControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "; Tag{" + tagToString()
        + "}; more info{" + moreInfo + "}";
    }

    String tagToString(){
        return tagDto == null ? null : tagDto.getName();
    }
}
