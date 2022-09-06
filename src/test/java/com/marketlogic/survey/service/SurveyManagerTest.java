package com.marketlogic.survey.service;

import com.marketlogic.survey.TestUtils;
import com.marketlogic.survey.data.SurveyRepository;
import com.marketlogic.survey.domain.exception.SurveyCouldNotBeCreatedException;
import com.marketlogic.survey.domain.Option;
import com.marketlogic.survey.domain.Question;
import com.marketlogic.survey.domain.Survey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurveyManagerTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyManager surveyManager;

    @Test
    void shouldAssignIdsToValidSurvey() throws SurveyCouldNotBeCreatedException {
        int surveyId = 89;
        when(surveyRepository.save(TestUtils.sampleSurvey())).thenReturn(savedSampleSurvey(surveyId));
        var createdSurvey = surveyManager.create(TestUtils.sampleSurvey());

        assertThat(createdSurvey.getId()).isEqualTo(surveyId);
        createdSurvey.setId(null);
        assertThat(createdSurvey).isEqualTo(TestUtils.sampleSurvey());
    }

    @Test
    void shouldThrowSurveyCouldNotBeCreatedExceptionForRuntimeException() {
        when(surveyRepository.save(TestUtils.sampleSurvey())).thenThrow(new RuntimeException());
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(TestUtils.sampleSurvey()));
    }

    @Test
    void shouldNotCreateSurveyForNull() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(null));
    }

    @Test
    void shouldNotCreateEmptySurvey() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(new Survey()));
    }

    @Test
    void shouldNotCreateExistingSurvey() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(savedSampleSurvey(123)));
    }

    @Test
    void shouldNotCreateSurveyWithNoQuestions() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(surveyHavingQuestions(List.of())));
    }

    @Test
    void shouldNotCreateSurveyWithNullQuestions() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(surveyHavingQuestions(List.of())));
    }

    @Test
    void shouldNotCreateSurveyWhenQuestionHasNoOptions() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(surveyHavingQuestionWithOptions(List.of())));
    }

    @Test
    void shouldNotCreateSurveyWhenQuestionHasNullOptions() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(surveyHavingQuestionWithOptions(null)));
    }

    @Test
    void shouldNotCreateSurveyWithoutTitle() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(TestUtils.sampleSurvey(null, "questionText")));
    }

    @Test
    void shouldNotCreateSurveyIfQuestionHasNoText() {
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(TestUtils.sampleSurvey("No question text", null)));
        assertThrows(SurveyCouldNotBeCreatedException.class, () -> surveyManager.create(TestUtils.sampleSurvey("No question text", "")));
    }

    @Test
    void shouldReturnExistingSurvey() {
        int existingSurveyId = 61;
        when(surveyRepository.findById(existingSurveyId)).thenReturn(Optional.of(savedSampleSurvey(existingSurveyId)));
        var existingSurvey = surveyManager.findSurveyWithId(existingSurveyId);

        assertThat(existingSurvey).isNotNull();
        assertThat(existingSurvey.isPresent()).isTrue();
        assertThat(existingSurvey.get()).isEqualTo(savedSampleSurvey(existingSurveyId));
    }

    @Test
    void shouldReturnEmptyOptionalForMissingSurvey() {
        int missingSurveyId = 394;
        when(surveyRepository.findById(missingSurveyId)).thenReturn(Optional.empty());
        var existingSurvey = surveyManager.findSurveyWithId(missingSurveyId);

        assertThat(existingSurvey).isNotNull();
        assertThat(existingSurvey.isEmpty()).isTrue();
    }

    private Survey surveyHavingQuestions(List<Question> questions) {
        var survey = new Survey();
        survey.setQuestions(questions);
        return survey;
    }

    private Survey surveyHavingQuestionWithOptions(List<Option> options) {
        Survey survey = new Survey();
        var question = new Question();
        question.setOptions(options);
        survey.setQuestions(List.of(question));
        return survey;
    }

    private Survey savedSampleSurvey(int id) {
        var survey = TestUtils.sampleSurvey();
        survey.setId(id);
        return survey;
    }



}