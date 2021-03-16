package com.example.apisample;

import com.example.apisample.controller.ApiSampleController;
import com.example.apisample.model.ApiSample;
import com.example.apisample.repository.ApiSampleRepository;
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

import static com.example.apisample.ApiDocumentUtils.getDocumentRequest;
import static com.example.apisample.ApiDocumentUtils.getDocumentResponse;
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

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ApiSampleController.class)
@AutoConfigureRestDocs
class ApiSampleApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApiSampleRepository apiSampleRepository;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    public void shouldCreateRecord() throws Exception {

        long id = 0l;
        //given
        when(apiSampleRepository.save(any(ApiSample.class)))
                .thenReturn(new ApiSample(id, "title", "description"));

        //when
        ResultActions result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/api-sample/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + id + ",\"title\":\"title\",\"description\":\"description\"}")
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/api-sample/" + id))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andDo(print())
                .andDo(document("api-sample-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("タイトル"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("説明")
                        ), responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("タイトル"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("説明")
                        )));
    }

    @Test
    public void shouldReturnRecords() throws Exception {

        //given
        ApiSample apiSample1 = new ApiSample(0, "title", "description");
        ApiSample apiSample2 = new ApiSample(1, "title1", "description1");
        List<ApiSample> apiSampleList = Arrays.asList(apiSample1, apiSample2);

        //when
        when(apiSampleRepository.findAll()).thenReturn(apiSampleList);
        ResultActions result = this.mockMvc.perform(get("/api/api-sample/")
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[1].title").value("title1"))
                .andDo(print())
                .andDo(document("api-sample-select-many",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @Test
    public void shouldReturnRecord() throws Exception {
        long id = 0l;

        //given
        ApiSample apiSample = new ApiSample(id, "title", "description");

        //when
        when(apiSampleRepository.findById(id)).thenReturn(java.util.Optional.of(apiSample));

        ResultActions result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/api-sample/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        result
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andDo(print())
                .andDo(document("api-sample-select-one",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("タイトル"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("説明")
                        )));
    }

    @Test
    public void shouldUpdateRecord() throws Exception {
        long id = 0l;

        //given
        ApiSample apiSample = new ApiSample(id, "title", "description");

        //when
        when(apiSampleRepository.findById(id)).thenReturn(java.util.Optional.of(apiSample));

        ResultActions result = this.mockMvc.perform(RestDocumentationRequestBuilders.put("/api/api-sample/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"string\",\"description\":\"string\"}")
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("api-sample-update",
                        getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    public void shouldDeleteRecord() throws Exception {
        long id = 0l;

        //given
        ApiSample apiSample = new ApiSample(id, "title", "description");

        //when
        when(apiSampleRepository.findById(0l)).thenReturn(java.util.Optional.of(apiSample));
        ResultActions result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/api-sample/{id}", id)
                .accept(MediaType.APPLICATION_JSON));
        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("api-sample-delete", getDocumentRequest(), getDocumentResponse()));
    }

}