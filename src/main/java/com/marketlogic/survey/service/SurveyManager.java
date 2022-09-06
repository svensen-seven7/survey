package com.marketlogic.survey.service;

import com.marketlogic.survey.data.SurveyRepository;
import com.marketlogic.survey.domain.exception.SurveyCouldNotBeCreatedException;
import com.marketlogic.survey.domain.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SurveyManager {

    private final SurveyRepository surveyRepository;

    public Survey create(Survey survey) throws SurveyCouldNotBeCreatedException {
        checkPreconditions(survey);

        try {
            var createdSurvey = surveyRepository.save(survey);
            log.info("Survey {} created", createdSurvey);
            return createdSurvey;
        } catch (Exception e) {
            log.error("Could not create survey {} due to {}", survey, e.getMessage());
            throw new SurveyCouldNotBeCreatedException(e);
        }
    }

    private void checkPreconditions(Survey survey) throws SurveyCouldNotBeCreatedException {
        if (survey == null) {
            throw new SurveyCouldNotBeCreatedException("Survey could not be null");
        }
        if (!survey.isValid()) {
            throw new SurveyCouldNotBeCreatedException("Survey is not valid");
        }
        if (survey.isExisting()) {
            throw new SurveyCouldNotBeCreatedException("Survey already exists");
        }
    }

    public Optional<Survey> findSurveyWithId(int surveyId) {
        return surveyRepository.findById(surveyId);
    }

}
