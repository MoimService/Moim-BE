package com.codeit.moim.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("token"))
                        .addParameters("size", new Parameter().in("query").name("size").description("페이지 크기").required(false))
                        .addParameters("sort", new Parameter().in("query").name("sort").description("정렬 기준").required(false))
                )
                .addSecurityItem(new SecurityRequirement().addList("token")) // 위에서 설정한 "token" 헤더 사용
                .info(new Info()
                        .title("Deving 백엔드 API 명세서")
                        .description("Deving 백엔드 API 명세서 입니다.")
                        .version("v0.0.1"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                        new Server().url("http://3.34.218.217:8080").description("운영 서버")
                ));
    }
}
