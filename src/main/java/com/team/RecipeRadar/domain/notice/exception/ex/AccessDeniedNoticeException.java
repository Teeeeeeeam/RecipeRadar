package com.team.RecipeRadar.domain.notice.exception.ex;

public class AccessDeniedNoticeException extends  RuntimeException{
    public AccessDeniedNoticeException(String message) {
        super(message);
    }
}
