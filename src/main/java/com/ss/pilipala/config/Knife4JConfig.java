package com.ss.pilipala.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class Knife4JConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("测试API")
                        .version("1.0")
                        .description("项目学习")
                        .termsOfService("http://localhost:8080/swaggeer-ui.html")
                );
    }
}