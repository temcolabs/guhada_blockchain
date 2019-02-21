package io.temco.guhada.blockchain.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.ant("/**"))
                .build()
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("[Temco] Temco Si-BlockChain APi")
                .description("Temco Si-BlockChain 과 연관된 API를 제공하는 페이지 입니다.")
                .contact(new Contact("Temco", "https://temco.io/", "support@temco.io"))
                .termsOfServiceUrl("https://temco.io/#/term")
                .version("Ver 0.0.1")
                .build()
                ;
    }
}