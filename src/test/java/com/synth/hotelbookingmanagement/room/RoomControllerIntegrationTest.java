package com.synth.hotelbookingmanagement.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.Random.class)
class RoomControllerIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired RoomRepository repository;
    @Autowired JdbcTemplate jdbcTemplate;
    private String hotelFixtureId;

    /** Returns the minimal valid JSON payload, substituting live parent IDs for FK fields. */
    private String payload() {
        return String.format("{\"roomNumber\":\"test-security\",\"type\":\"SINGLE\",\"floor\":1,\"pricePerNight\":1.0,\"capacity\":1,\"status\":\"AVAILABLE\",\"hotelId\":\"%s\"}", hotelFixtureId);
    }

    @BeforeAll
    static void disableRyuk() {
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }

    @BeforeEach
    void setUp() throws Exception {
        jdbcTemplate.execute("TRUNCATE TABLE rooms CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE hotels CASCADE");
        {
            String _loc = mockMvc.perform(post("/api/v1/hotels")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"parent-security\",\"address\":\"parent-security\",\"city\":\"parent-security\",\"country\":\"parent-security\",\"starRating\":1,\"phone\":\"+1-555-0200\",\"email\":\"parent@example.com\",\"status\":\"ACTIVE\"}"))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getHeader("Location");
            hotelFixtureId = _loc.substring(_loc.lastIndexOf('/') + 1);
        }
    }

    // ── GET /api/v1/rooms ─────────────────────────────────────────────────────────

    @Test
    void should_return_200_with_empty_list_when_no_entities_exist() throws Exception {
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void should_return_200_with_entities_after_create() throws Exception {
        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    // ── GET /api/v1/rooms/{id} ───────────────────────────────────────────────────

    @Test
    void should_return_200_when_entity_exists_by_id() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.roomNumber").value("test-security"))
                .andExpect(jsonPath("$.type").value("SINGLE"))
                .andExpect(jsonPath("$.floor").value(1))
                .andExpect(jsonPath("$.pricePerNight").value(1.0))
                .andExpect(jsonPath("$.capacity").value(1))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void should_return_404_when_entity_is_not_found_by_id() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void should_return_400_when_id_format_is_invalid() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/{id}", "not-a-valid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ── POST /api/v1/rooms ────────────────────────────────────────────────────────

    @Test
    void should_return_201_and_persist_entity_when_valid_request_is_provided() throws Exception {
        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.roomNumber").value("test-security"))
                .andExpect(jsonPath("$.type").value("SINGLE"))
                .andExpect(jsonPath("$.floor").value(1))
                .andExpect(jsonPath("$.pricePerNight").value(1.0))
                .andExpect(jsonPath("$.capacity").value(1))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        assertThat(repository.count()).isEqualTo(1);
    }
    @Test
    void should_return_400_when_create_payload_is_empty() throws Exception {
        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }
    @Test
    void should_return_400_when_create_request_has_blank_string_fields() throws Exception {
        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomNumber\":\"\",\"type\":\"\",\"floor\":0,\"pricePerNight\":0.0,\"capacity\":0,\"status\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ── PUT /api/v1/rooms/{id} ────────────────────────────────────────────────────

    @Test
    void should_return_200_and_update_entity_when_valid_request_is_provided() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void should_return_404_when_updating_non_existent_entity() throws Exception {
        mockMvc.perform(put("/api/v1/rooms/{id}", "00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
    @Test
    void should_return_400_when_update_payload_is_empty() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ── DELETE /api/v1/rooms/{id} ─────────────────────────────────────────────────

    @Test
    void should_return_204_and_remove_entity_when_id_exists() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    void should_return_404_when_deleting_non_existent_entity() throws Exception {
        mockMvc.perform(delete("/api/v1/rooms/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ── Lifecycle & data-integrity ─────────────────────────────────────────────

    @Test
    void should_return_404_when_fetching_entity_after_delete() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void should_return_404_when_entity_is_deleted_twice() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(delete(location)).andExpect(status().isNotFound());
    }

    @Test
    void should_return_empty_list_when_all_entities_are_deleted() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }
    @Test
    void should_return_400_when_update_request_has_blank_string_fields() throws Exception {
        String location = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomNumber\":\"\",\"type\":\"\",\"floor\":0,\"pricePerNight\":0.0,\"capacity\":0,\"status\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ── Correlation ID ─────────────────────────────────────────────────────────

    @Test
    void should_echo_correlation_id_header_when_provided_in_request() throws Exception {
        mockMvc.perform(get("/api/v1/rooms")
                        .header("X-Request-ID", "test-trace-abc-123"))
                .andExpect(header().string("X-Request-ID", "test-trace-abc-123"));
    }

    @Test
    void should_generate_correlation_id_header_when_absent_from_request() throws Exception {
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(header().exists("X-Request-ID"));
    }
}
