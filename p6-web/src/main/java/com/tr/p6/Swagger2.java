package com.tr.p6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
	
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
	            .apiInfo(apiInfo())
	            .tags(new Tag("Staff", "职员"), getTags())
	            .select()
	            .apis(RequestHandlerSelectors.basePackage("com.tr"))
	            .paths(PathSelectors.any())
	            .build();
    }
    
    private Tag[] getTags() {
	    	Tag[] tags = {
	    		new Tag("Depot", "仓库"),
	    		new Tag("Exception", "异常"),
	    		new Tag("Properties", "配置")
	    	};
	    	return tags;
    }
    
	private ApiInfo apiInfo() {
    		Contact contact = new Contact("TR", "http://www.tr.com/", "tr1838@163.com");
        return new ApiInfoBuilder()
	            .title("Parent-Project6")
	            .description("接口文档 api")
	            .termsOfServiceUrl("http://www.baidu.com/")
	            .contact(contact)
	            .version("1.0")
	            .build();
    }
    
}
