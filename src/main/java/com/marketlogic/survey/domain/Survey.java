
package com.marketlogic.survey.domain;

import java.util.List;
import lombok.Data;

@Data
public class Survey {

    private Integer id;
    private String title;
    private List<Question> questions;

    public boolean isValid() {
        return title != null && !title.isEmpty()
                && questions != null && !questions.isEmpty() && questions.stream().allMatch(Question::isValid);
    }

    public boolean isExisting() {
        return id != null;
    }

}
