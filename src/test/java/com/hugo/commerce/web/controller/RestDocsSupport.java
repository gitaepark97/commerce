package com.hugo.commerce.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;

    protected abstract Object initController();

    protected List<HandlerMethodArgumentResolver> argumentResolvers() {
        return List.of();
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .setControllerAdvice(new ApiControllerAdvice())
            .setCustomArgumentResolvers(argumentResolvers().toArray(new HandlerMethodArgumentResolver[0]))
            .apply(documentationConfiguration(provider))
            .build();
    }

}
