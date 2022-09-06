package com.marketlogic.survey.data;

import com.marketlogic.survey.domain.Survey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@Slf4j
public class InMemorySurveyRepository implements SurveyRepository {

    private final List<Survey> savedSurveys = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public Survey save(Survey survey) {
        readWriteLock.writeLock().lock();
        try {
            survey.setId(savedSurveys.size() + 1);
            savedSurveys.add(survey);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return survey;
    }

    @Override
    public Optional<Survey> findById(int surveyId) {
        readWriteLock.readLock().lock();
        try {
            if (surveyId > 0 && surveyId <= savedSurveys.size()) {
                return Optional.of(savedSurveys.get(surveyId - 1));
            } else {
                log.error("Could not find survey with id = {}", surveyId);
                return Optional.empty();
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

}
