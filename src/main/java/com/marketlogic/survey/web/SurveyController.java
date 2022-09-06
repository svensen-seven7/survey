package com.marketlogic.survey.web;

import com.marketlogic.survey.domain.Survey;
import com.marketlogic.survey.domain.exception.SurveyCouldNotBeCreatedException;
import com.marketlogic.survey.service.SurveyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1/surveys")
@RequiredArgsConstructor
@Slf4j
public class SurveyController {

    private final SurveyManager surveyManager;

    @PostMapping
    public ResponseEntity<Object> createSurvey(@RequestBody Survey survey) throws URISyntaxException {
        log.info("createSurvey is called for {}", survey);
        try {
            Survey persistedSurvey = surveyManager.create(survey);
            URI uri = new URI("/v1/surveys/" + persistedSurvey.getId());
            return ResponseEntity.created(uri).body(persistedSurvey);
        } catch (SurveyCouldNotBeCreatedException e) {
            log.error("Could not create survey {} due to {}", survey, e.getMessage());
            return ResponseEntity.badRequest().body(survey);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable int id) {
        log.info("getSurvey is called for id = {}", id);
        return surveyManager.findSurveyWithId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
