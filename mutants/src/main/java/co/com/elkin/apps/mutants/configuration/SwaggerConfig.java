package co.com.elkin.apps.mutants.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for swagger documentation
 * 
 * @author egiraldo
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String MAINTAINER = "elkin.giraldo.pinedo@gmail.com";
	private static final Set<String> DEFAULT_PRODUCES = new HashSet<>(Arrays.asList("application/json"));
	private static final Set<String> DEFAULT_CONSUMES = new HashSet<>(Arrays.asList("application/json"));

	public static final Contact DEFAULT_CONTACT = new Contact("Elkin Giraldo", MAINTAINER, MAINTAINER);
	public static final ApiInfo DEFAULT = new ApiInfo("Api Documentation for Mutants API",
			"This module is in charge of managing all request to Mutants API", "1.0", "urn:tos", DEFAULT_CONTACT,
			"Elkin Giraldo rights reserved", MAINTAINER, new ArrayList<>());

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT).produces(DEFAULT_PRODUCES)
				.consumes(DEFAULT_CONSUMES);
	}

}
