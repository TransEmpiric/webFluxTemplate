package com.transempiric.webfluxTemplate;

import com.transempiric.webfluxTemplate.config.web.WebConfig;
import com.transempiric.webfluxTemplate.mongo.MongoConfiguration;
import com.transempiric.webfluxTemplate.web.test.TestRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@WebFluxTest(TestRestController.class)
@ContextConfiguration(classes = {WebfluxTemplateApplication.class, WebConfig.class, MongoConfiguration.class})
public class WebfluxTemplateApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private WebTestClient webTestClient;

    @Test
    public void fetchUsers() {

        String result = webTestClient
                .get().uri("/test/user/jdev")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("username").isEqualTo("jdev")
                .jsonPath("firstname").isEqualTo("Joe")
                .jsonPath("lastname").isEqualTo("Developer")
                .jsonPath("email").isEqualTo("dev@transempiric.com")
                .returnResult()
                .toString();

        logger.info(result.toString());
    }

}