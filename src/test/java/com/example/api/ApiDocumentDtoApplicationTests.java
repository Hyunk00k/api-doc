package com.example.api;

import static com.example.api.ApiDocumentUtils.getDocumentRequest;
import static com.example.api.ApiDocumentUtils.getDocumentResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.api.controller.ApiDocumentController;
import com.example.api.dto.ApiDocumentDto;
import com.example.api.repository.ApiDocumentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ApiDocumentController.class)
@AutoConfigureRestDocs
class ApiDocumentDtoApplicationTests {

    @Autowired private MockMvc mockMvc;
    @MockBean private ApiDocumentRepository apiDocumentRepository;

    @BeforeEach
    public void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation))
                        .build();
    }

    @Test
    public void shouldCreateRecord() throws Exception {

        long id = 0l;
        // given
        when(apiDocumentRepository.save(any(ApiDocumentDto.class)))
                .thenReturn(new ApiDocumentDto(id, "title", "description"));

        // when
        ResultActions result =
                this.mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/api-document/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"id\":"
                                                + id
                                                + ",\"title\":\"title\",\"description\":\"description\"}")
                                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/api-document/" + id))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andDo(print())
                .andDo(
                        document(
                                "api-document-create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(),
                                requestFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("ID"),
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING)
                                                .description("タイトル"),
                                        fieldWithPath("description")
                                                .type(JsonFieldType.STRING)
                                                .description("説明")),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("ID"),
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING)
                                                .description("タイトル"),
                                        fieldWithPath("description")
                                                .type(JsonFieldType.STRING)
                                                .description("説明"))));
    }

    @Test
    public void shouldReturnRecords() throws Exception {

        // given
        ApiDocumentDto apiDocumentDto1 = new ApiDocumentDto(0, "title", "description");
        ApiDocumentDto apiDocumentDto2 = new ApiDocumentDto(1, "title1", "description1");
        List<ApiDocumentDto> apiDocumentDtoList = Arrays.asList(apiDocumentDto1, apiDocumentDto2);

        // when
        when(apiDocumentRepository.findAll()).thenReturn(apiDocumentDtoList);
        ResultActions result =
                this.mockMvc.perform(get("/api/api-document/").accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[1].title").value("title1"))
                .andDo(print())
                .andDo(
                        document(
                                "api-document-select-many",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    public void shouldReturnRecord() throws Exception {
        long id = 0l;

        // given
        ApiDocumentDto apiDocumentDto = new ApiDocumentDto(id, "title", "description");

        // when
        when(apiDocumentRepository.findById(id)).thenReturn(java.util.Optional.of(apiDocumentDto));

        ResultActions result =
                this.mockMvc
                        .perform(
                                RestDocumentationRequestBuilders.get("/api/api-document/{id}", id)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        // then
        result.andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andDo(print())
                .andDo(
                        document(
                                "api-document-select-one",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("id").description("ID").optional()),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("ID"),
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING)
                                                .description("タイトル"),
                                        fieldWithPath("description")
                                                .type(JsonFieldType.STRING)
                                                .description("説明"))));
    }

    @Test
    public void shouldUpdateRecord() throws Exception {
        long id = 0l;

        // given
        ApiDocumentDto apiDocumentDto = new ApiDocumentDto(id, "title", "description");

        // when
        when(apiDocumentRepository.findById(id)).thenReturn(java.util.Optional.of(apiDocumentDto));

        ResultActions result =
                this.mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/api-document/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"string\",\"description\":\"string\"}")
                                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "api-document-update",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    public void shouldDeleteRecord() throws Exception {
        long id = 0l;

        // given
        ApiDocumentDto apiDocumentDto = new ApiDocumentDto(id, "title", "description");

        // when
        when(apiDocumentRepository.findById(0l)).thenReturn(java.util.Optional.of(apiDocumentDto));
        ResultActions result =
                this.mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/api-document/{id}", id)
                                .accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document(
                                "api-document-delete",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }
}
