package com.transempiric.webfluxTemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.transempiric.webfluxTemplate.config.web",
        "com.transempiric.webfluxTemplate.config.security",
        "com.transempiric.webfluxTemplate.config.websocket",
        "com.transempiric.webfluxTemplate.mongo",
        "com.transempiric.webfluxTemplate.service",
        "com.transempiric.webfluxTemplate.web"
})
@EnableConfigurationProperties({MongoProperties.class})
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
})
public class WebfluxTemplateApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebfluxTemplateApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
	}
}
