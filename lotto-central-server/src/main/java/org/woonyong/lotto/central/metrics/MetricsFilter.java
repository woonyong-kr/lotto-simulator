package org.woonyong.lotto.central.metrics;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsFilter extends OncePerRequestFilter {

    private final MetricsCollector metricsCollector;

    public MetricsFilter(final MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        if (shouldSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        measureAndRecord(request, response, filterChain);
    }

    private boolean shouldSkip(final HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/sse") || path.startsWith("/actuator");
    }

    private void measureAndRecord(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        boolean success = true;

        try {
            filterChain.doFilter(request, response);
            success = isSuccessStatus(response.getStatus());
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            long responseTime = System.currentTimeMillis() - startTime;
            metricsCollector.recordRequest(responseTime, success);
        }
    }

    private boolean isSuccessStatus(final int status) {
        return status >= 200 && status < 400;
    }
}
