package com.epam.lab.exception;

import com.epam.lab.dto.NewsDto;

public class NewsControllerException extends ControllerException {
    private NewsDto newsDto;
    private String moreInfo;

    public NewsControllerException(){

    }

    public NewsControllerException(String message){
        super(message);
    }

    public NewsControllerException(NewsDto newsDto, String message) {
        super(message);
        this.newsDto = newsDto;
    }

    public NewsControllerException(NewsDto newsDto, String message, String moreInfo) {
        super(message);
        this.newsDto = newsDto;
        this.moreInfo = moreInfo;
    }
    public NewsControllerException(NewsDto newsDto, String message, String moreInfo, int responseCod) {
        super(message, responseCod);
        this.newsDto = newsDto;
        this.moreInfo = moreInfo;
    }
    public NewsControllerException(NewsDto newsDto, String message, String moreInfo, Throwable cause) {
        super(message, cause);
        this.newsDto = newsDto;
        this.moreInfo = moreInfo;
    }
    public NewsControllerException(NewsDto newsDto, String message, String moreInfo, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
        this.newsDto = newsDto;
        this.moreInfo = moreInfo;
    }

    public NewsControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsControllerException(String message, Throwable cause, int responseCod) {
        super(message, cause, responseCod);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "; News{"
                + newsToString() + "}; more info{"
                + moreInfo + "}";
    }

   private String newsToString(){
       return (newsDto == null ? null : newsDto.getTitle() + ", " + newsDto.getShortText()
       + ", " + newsDto.getFullText() + ", " + newsDto.getCreationDate() + ", " + newsDto.getModificationDate());
   }
}
