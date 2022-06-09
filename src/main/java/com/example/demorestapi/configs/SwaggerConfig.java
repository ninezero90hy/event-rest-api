package com.example.demorestapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private String version;
	private String title;

	@Bean
	public Docket apiV1() {
		version = "V1";
		title = "victolee API " + version;

		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName(version)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demorestapi.events"))
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.apiInfo(apiInfo(title, version));

	}

	private ApiInfo apiInfo(String title, String version) {
		return new ApiInfo(
				title,
				"Swagger로 생성한 API Docs",
				version,
				"www.example.com",
				new Contact("Contact Me", "www.example.com", "foo@example.com"),
				"Licenses",
				"www.example.com",
				new ArrayList<>());
	}

	// swagger와 hateoas 문제 해결하려면 이거 넣어야함
	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}
}
