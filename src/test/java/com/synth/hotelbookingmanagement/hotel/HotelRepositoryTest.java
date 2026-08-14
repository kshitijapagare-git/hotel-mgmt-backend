package com.synth.hotelbookingmanagement.hotel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@TestMethodOrder(MethodOrderer.Random.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelRepositoryTest {

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
        registry.add("spring.flyway.enabled", () -> "false");
        registry.add("spring.liquibase.enabled", () -> "false");
    }

    @Autowired
    private HotelRepository repository;

    @BeforeAll
    static void disableRyuk() {
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Hotel newEntity(int seed) {
        Hotel entity = new Hotel();
        
        entity.setName("test-security" + seed);
        
        entity.setAddress("test-security" + seed);
        
        entity.setCity("test-security" + seed);
        
        entity.setCountry("test-security" + seed);
        
        entity.setStarRating(seed + 1);
        
        entity.setPhone("+1-555-0100" + seed);
        
        entity.setEmail("test@example.com" + seed);
        
        entity.setStatus("ACTIVE");
        return entity;
    }

    // ── Save ───────────────────────────────────────────────────────────────────

    @Test
    void should_persist_entity_and_assign_id_when_saved() {
        Hotel saved = repository.save(newEntity(0));
        assertThat(saved.getId()).isNotNull();
    }

    // ── FindById ───────────────────────────────────────────────────────────────

    @Test
    void should_return_present_when_entity_exists_by_id() {
        Hotel saved = repository.save(newEntity(0));
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void should_return_empty_when_entity_is_not_found() {
        assertThat(repository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).isEmpty();
    }

    // ── FindAll ────────────────────────────────────────────────────────────────

    @Test
    void should_return_all_entities_when_repository_is_queried() {
        repository.save(newEntity(1));
        repository.save(newEntity(2));
        Page<Hotel> page = repository.findAll((Specification<Hotel>) null, PageRequest.of(0, 20));
        assertThat(page.getContent()).hasSize(2);
    }

    // ── Delete ─────────────────────────────────────────────────────────────────

    @Test
    void should_remove_entity_when_delete_is_called() {
        Hotel saved = repository.save(newEntity(0));
        repository.delete(saved);
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    void should_return_zero_count_when_repository_is_empty() {
        assertThat(repository.count()).isZero();
    }

    // ── Specification ──────────────────────────────────────────────────────────

    @Test
    void should_return_all_entities_when_specification_is_null() {
        repository.save(newEntity(0));
        Page<Hotel> page = repository.findAll((Specification<Hotel>) null, PageRequest.of(0, 20));
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void should_filter_results_when_specification_is_provided() {
        repository.save(newEntity(0));
        repository.save(newEntity(1));
        Specification<Hotel> matchNone = (root, query, cb) -> cb.disjunction();
        Page<Hotel> page = repository.findAll(matchNone, PageRequest.of(0, 20));
        assertThat(page.getContent()).isEmpty();
    }
}
