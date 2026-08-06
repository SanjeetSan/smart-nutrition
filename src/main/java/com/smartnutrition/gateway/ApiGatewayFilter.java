package com.smartnutrition.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiGatewayFilter extends OncePerRequestFilter {

    private final GatewayMetricsService gatewayMetricsService;

    public ApiGatewayFilter(GatewayMetricsService gatewayMetricsService) {
        this.gatewayMetricsService = gatewayMetricsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        String targetMicroservice = resolveMicroserviceRoute(path);

        // Attach Gateway routing metadata to response headers
        response.setHeader("X-Gateway-Router", "SmartNutrition-InProject-Gateway-v1");
        response.setHeader("X-Microservice-Route", targetMicroservice);

        // Record metrics for the targeted microservice module
        gatewayMetricsService.recordRequest(targetMicroservice);

        filterChain.doFilter(request, response);
    }

    private String resolveMicroserviceRoute(String path) {
        if (path.startsWith("/api/auth") || path.startsWith("/api/admin")) {
            return "AUTH-ADMIN-MICROSERVICE";
        } else if (path.startsWith("/api/meals") || path.startsWith("/api/assistant")) {
            return "AI-MEAL-VISION-MICROSERVICE";
        } else if (path.startsWith("/api/teacher") || path.startsWith("/api/parent")) {
            return "SCHOOL-REPORT-MICROSERVICE";
        } else if (path.startsWith("/api/messages") || path.startsWith("/api/social")) {
            return "MESSAGING-SOCIAL-MICROSERVICE";
        }
        return "GENERAL-GATEWAY-ROUTE";
    }
}
