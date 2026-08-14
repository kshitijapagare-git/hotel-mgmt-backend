package com.synth.hotelbookingmanagement.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    // ── Header absent ──────────────────────────────────────────────────────────

    @Test
    void should_generate_uuid_correlation_id_when_header_is_absent() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader("X-Request-ID"))
                .isNotNull()
                .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    // ── Header present ─────────────────────────────────────────────────────────

    @Test
    void should_preserve_upstream_correlation_id_when_header_is_present() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-ID", "upstream-trace-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader("X-Request-ID")).isEqualTo("upstream-trace-123");
    }

    // ── Header blank ───────────────────────────────────────────────────────────

    @Test
    void should_generate_uuid_correlation_id_when_header_is_blank() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-ID", "   ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader("X-Request-ID"))
                .isNotNull()
                .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    // ── MDC lifecycle ──────────────────────────────────────────────────────────

    @Test
    void should_set_mdc_correlation_id_during_request_processing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-ID", "trace-for-mdc");
        MockHttpServletResponse response = new MockHttpServletResponse();

        String[] captured = new String[1];
        FilterChain capturingChain = (ServletRequest req, ServletResponse res) -> {
            captured[0] = MDC.get("correlationId");
        };

        filter.doFilter(request, response, capturingChain);

        assertThat(captured[0]).isEqualTo("trace-for-mdc");
    }

    @Test
    void should_clear_mdc_after_request_is_processed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(MDC.get("correlationId")).isNull();
    }
}
