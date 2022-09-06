package com.marketlogic.survey.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketlogic.survey.domain.Survey;
import com.marketlogic.survey.domain.exception.SurveyCouldNotBeCreatedException;
import com.marketlogic.survey.service.SurveyManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SurveyController.class)
class SurveyControllerTest {

    private static final String BASE_URL = "/v1/surveys";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SurveyManager surveyManager;

    @Test
    public void shouldCreateSurveyWithOneQuestion() throws Exception {
        int surveyId = 2;
        when(surveyManager.create(any())).thenReturn(surveyFromFile(surveyId, "one-question-survey.json"));

        doPostFor(jsonFromFile("one-question-survey.json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE_URL + "/" + surveyId))
                .andExpect(content().json(jsonFromFile("one-question-survey-saved.json")));
    }

    @Test
    public void shouldCreateSurveyWithTwoQuestions() throws Exception {
        int surveyId = 15;
        when(surveyManager.create(any())).thenReturn(surveyFromFile(surveyId, "two-questions-survey.json"));

        doPostFor(jsonFromFile("two-questions-survey.json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE_URL + "/" + surveyId))
                .andExpect(content().json(jsonFromFile("two-questions-survey-saved.json")));
    }

    @Test
    public void shouldNotCreateSurveyForNullInput() throws Exception {
        mockMvc.perform(post(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateSurveyForEmptyInput() throws Exception {
        when(surveyManager.create(any(Survey.class))).thenThrow(SurveyCouldNotBeCreatedException.class);
        doPostFor("{}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateSurveyWithNoQuestions() throws Exception {
        var survey = objectMapper.readValue(jsonFromFile("survey-with-no-questions.json"), Survey.class);
        when(surveyManager.create(survey)).thenThrow(SurveyCouldNotBeCreatedException.class);

        doPostFor(jsonFromFile("survey-with-no-questions.json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateSurveyForQuestionsWithNoOptions() throws Exception {
        when(surveyManager.create(any(Survey.class))).thenThrow(SurveyCouldNotBeCreatedException.class);

        doPostFor(jsonFromFile("survey-with-no-options-for-questions.json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundStatusForMissingSurvey() throws Exception {
        mockMvc.perform(get("/surveys/v1/18").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnExistingSurvey() throws Exception {
        var survey = surveyFromFile(12, "two-questions-survey.json");
        when(surveyManager.findSurveyWithId(12)).thenReturn(Optional.of(survey));
        mockMvc.perform(get(BASE_URL + "/12").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(survey)));
    }

    private ResultActions doPostFor(String content) throws Exception {
        return mockMvc.perform(post(BASE_URL).content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private String jsonFromFile(String filename) throws URISyntaxException, IOException {
        return Files.readString(Path.of(getClass().getClassLoader().getResource(filename).toURI()));
    }

    private Survey surveyFromFile(int id, String filename) throws IOException {
        var survey = objectMapper.readValue(getClass().getClassLoader().getResource(filename), Survey.class);
        survey.setId(id);
        return survey;
    }

}
