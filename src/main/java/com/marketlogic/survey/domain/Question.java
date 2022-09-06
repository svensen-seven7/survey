
package com.marketlogic.survey.domain;

import lombok.Data;

import java.util.List;

@Data
public class Question {

    private List<Option> options;

    private String text;

    public boolean isValid() {
        return text != null && !text.isEmpty() && options != null && !options.isEmpty();
    }

}
