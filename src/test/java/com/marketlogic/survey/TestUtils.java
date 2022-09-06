package com.marketlogic.survey;

import com.marketlogic.survey.domain.Option;
import com.marketlogic.survey.domain.Question;
import com.marketlogic.survey.domain.Survey;

import java.util.List;

public class TestUtils {

    public static Survey sampleSurvey() {
        var validSurvey = sampleSurvey("Sample survey title", "questionText");
        if (!validSurvey.isValid()) {
            throw new IllegalStateException("sampleSurvey is not valid");
        }
        return validSurvey;
    }

    public static Survey sampleSurvey(String title, String questionText) {
        var survey = new Survey();
        var question = new Question();
        question.setText(questionText);
        question.setOptions(List.of(new Option("Sample option")));
        survey.setQuestions(List.of(question));
        survey.setTitle(title);

        return survey;
    }

}
