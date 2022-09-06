package com.marketlogic.survey.data;

import com.marketlogic.survey.TestUtils;
import com.marketlogic.survey.domain.Survey;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class InMemorySurveyRepositoryTest {

    private SurveyRepository repository = new InMemorySurveyRepository();

    @Test
    void shouldSaveSingleSurvey() {
        var savedSurvey = repository.save(TestUtils.sampleSurvey());
        assertThat(savedSurvey).isNotNull();

        var savedSurveyId = savedSurvey.getId();
        assertThat(savedSurveyId).isNotZero();

        savedSurvey.setId(null);
        assertThat(savedSurvey).isEqualTo(TestUtils.sampleSurvey());
        savedSurvey.setId(savedSurveyId);
    }

    @Test
    void shouldAssignUniqueIdToSurveys() {
        List<Integer> savedSurveyIds = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            var savedSurvey = repository.save(TestUtils.sampleSurvey());
            savedSurveyIds.add(savedSurvey.getId());
        }
        assertThat(savedSurveyIds).doesNotHaveDuplicates();
    }

    @Test
    void shouldFindSavedSurvey() {
        var savedSurvey = repository.save(TestUtils.sampleSurvey());
        var fetchedSurvey = repository.findById(savedSurvey.getId());
        assertThat(fetchedSurvey.get()).isEqualTo(savedSurvey);
    }

    @Test
    void shouldReturnEmptyResultForMissingSurvey() {
        assertThat(repository.findById(-1)).isEmpty();
        assertThat(repository.findById(0)).isEmpty();
        assertThat(repository.findById(1)).isEmpty();
    }

    @Test
    void testThreadSafety() {
        var savedSurveys = IntStream.range(0, 100_000)
                .parallel()
                .mapToObj(i -> repository.save(TestUtils.sampleSurvey()))
                .collect(Collectors.toList());
        assertThat(savedSurveys.stream().mapToInt(Survey::getId)).doesNotHaveDuplicates();
    }

}