
package com.marketlogic.survey.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Option {

    public Option(String value) {
        this.value = value;
    }

    private String value;

}
