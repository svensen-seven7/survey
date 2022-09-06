package com.marketlogic.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketlogic.survey.domain.Survey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void shouldCreateAndRetrieveSurvey() throws Exception {
		var baseUrl = "http://localhost:" + port + "/v1/surveys/";
		var sampleSurvey = surveyFromFile("two-questions-survey.json");
		var savedSurvey = restTemplate.postForEntity(baseUrl, sampleSurvey, Survey.class).getBody();
		var fetchedSurvey = restTemplate.getForObject(baseUrl + savedSurvey.getId(), Survey.class);

		assertThat(fetchedSurvey).isEqualTo(savedSurvey);
	}

	private Survey surveyFromFile(String filename) throws IOException {
		return objectMapper.readValue(getClass().getClassLoader().getResource(filename), Survey.class);
	}
}
