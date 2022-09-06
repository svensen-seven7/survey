package com.marketlogic.survey.data;

import com.marketlogic.survey.domain.Survey;

import java.util.Optional;

public interface SurveyRepository {

    Survey save(Survey survey);

    Optional<Survey> findById(int surveyId);

}
