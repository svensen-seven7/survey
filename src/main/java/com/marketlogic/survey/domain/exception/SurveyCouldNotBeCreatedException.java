package com.marketlogic.survey.domain.exception;

public class SurveyCouldNotBeCreatedException extends Exception {

    public SurveyCouldNotBeCreatedException(Exception e) {
        super(e);
    }

    public SurveyCouldNotBeCreatedException(String message) {
        super(message);
    }

}
