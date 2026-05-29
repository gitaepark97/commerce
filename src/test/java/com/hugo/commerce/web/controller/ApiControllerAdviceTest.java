package com.hugo.commerce.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiControllerAdviceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .setControllerAdvice(new ApiControllerAdvice())
            .build();
    }

    @Test
    @DisplayName("CoreException ERROR 레벨 발생 시 500 반환")
    void returns500_whenCoreExceptionWithErrorLevel() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/core-error"));

        // then
        result.andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.result").value("ERROR"))
            .andExpect(jsonPath("$.error.code").value("DEFAULT_ERROR"))
            .andExpect(jsonPath("$.error.message").value(ErrorType.DEFAULT_ERROR.getMessage()));
    }

    @Test
    @DisplayName("CoreException WARN 레벨 발생 시 400 반환")
    void returns400_whenCoreExceptionWithWarnLevel() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/core-warn"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.result").value("ERROR"))
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.error.message").value(ErrorType.BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("CoreException 발생 시 data 포함하여 반환")
    void returnsErrorWithData_whenCoreExceptionHasData() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/core-data"));

        // then
        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("NOT_FOUND"))
            .andExpect(jsonPath("$.error.data").value("item:1"));
    }

    @Test
    @DisplayName("유효성 검사 실패 시 필드 에러 목록 반환")
    void returnsFieldErrors_whenMethodArgumentNotValid() throws Exception {
        // given
        String body = objectMapper.writeValueAsString(new TestRequest(""));

        // when
        var result = mockMvc.perform(post("/test/valid")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.error.data[0].field").value("name"))
            .andExpect(jsonPath("$.error.data[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("제약 조건 위반 시 400 반환")
    void returns400_whenConstraintViolation() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/constraint"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("잘못된 JSON 요청 시 400 반환")
    void returns400_whenHttpMessageNotReadable() throws Exception {
        // when
        var result = mockMvc.perform(post("/test/valid")
            .contentType(MediaType.APPLICATION_JSON)
            .content("invalid json"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("필수 요청 파라미터 누락 시 400 반환")
    void returns400_whenRequiredRequestParamMissing() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/param"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("경로 변수 타입 불일치 시 400 반환")
    void returns400_whenPathVariableTypeMismatch() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/path/not-a-number"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("존재하지 않는 URL 요청 시 404 반환")
    void returns404_whenNoResourceFound() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/not-found"));

        // then
        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("지원하지 않는 HTTP 메서드 요청 시 405 반환")
    void returns405_whenHttpMethodNotSupported() throws Exception {
        // when
        var result = mockMvc.perform(delete("/test/param").param("value", "test"));

        // then
        result.andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.error.code").value("METHOD_NOT_ALLOWED"))
            .andExpect(jsonPath("$.error.message").value(ErrorType.METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    @DisplayName("예상치 못한 예외 발생 시 500 반환")
    void returns500_whenUnexpectedException() throws Exception {
        // when
        var result = mockMvc.perform(get("/test/error"));

        // then
        result.andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error.code").value("DEFAULT_ERROR"))
            .andExpect(jsonPath("$.error.message").value(ErrorType.DEFAULT_ERROR.getMessage()));
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/core-error")
        void coreError() {
            throw new CoreException(ErrorType.DEFAULT_ERROR);
        }

        @GetMapping("/core-warn")
        void coreWarn() {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }

        @GetMapping("/core-data")
        void coreData() {
            throw new CoreException(ErrorType.NOT_FOUND, "item:1");
        }

        @PostMapping("/valid")
        void valid(@RequestBody @jakarta.validation.Valid TestRequest request) {
        }

        @GetMapping("/constraint")
        void constraint() {
            throw new ConstraintViolationException(Set.of());
        }

        @GetMapping("/param")
        void param(@RequestParam String value) {
        }

        @GetMapping("/path/{id}")
        void path(@PathVariable Long id) {
        }

        @GetMapping("/not-found")
        void notFound() throws NoResourceFoundException {
            throw new NoResourceFoundException(org.springframework.http.HttpMethod.GET, "/test/not-found", "/test/not-found");
        }

        @GetMapping("/error")
        void error() {
            throw new RuntimeException("unexpected error");
        }

    }

    record TestRequest(@NotBlank String name) {

    }

}
