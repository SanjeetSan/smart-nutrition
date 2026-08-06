package com.smartnutrition.gateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
@Tag(name = "API Gateway Router", description = "In-Project API Gateway routes & microservice metrics")
public class GatewayController {

    private final GatewayMetricsService gatewayMetricsService;

    public GatewayController(GatewayMetricsService gatewayMetricsService) {
        this.gatewayMetricsService = gatewayMetricsService;
    }

    @GetMapping("/routes")
    @Operation(summary = "Get registered microservice routes in the gateway")
    public ResponseEntity<List<Map<String, String>>> getRoutes() {
        List<Map<String, String>> routes = List.of(
            Map.of("pathPrefix", "/api/auth/**", "targetMicroservice", "AUTH-ADMIN-MICROSERVICE", "description", "Authentication & JWT Token Service"),
            Map.of("pathPrefix", "/api/admin/**", "targetMicroservice", "AUTH-ADMIN-MICROSERVICE", "description", "School Admin & User Management Service"),
            Map.of("pathPrefix", "/api/meals/**", "targetMicroservice", "AI-MEAL-VISION-MICROSERVICE", "description", "Gemini 1.5 Flash Meal Vision & Leftover Engine"),
            Map.of("pathPrefix", "/api/assistant/**", "targetMicroservice", "AI-MEAL-VISION-MICROSERVICE", "description", "Conversational AI Health Assistant"),
            Map.of("pathPrefix", "/api/teacher/**", "targetMicroservice", "SCHOOL-REPORT-MICROSERVICE", "description", "Class Rosters & Weekly/Monthly Health Reports"),
            Map.of("pathPrefix", "/api/parent/**", "targetMicroservice", "SCHOOL-REPORT-MICROSERVICE", "description", "Parent 1-Time Onboarding & Linked Students"),
            Map.of("pathPrefix", "/api/messages/**", "targetMicroservice", "MESSAGING-SOCIAL-MICROSERVICE", "description", "Direct 1-on-1 Parent-Teacher Messaging"),
            Map.of("pathPrefix", "/api/social/**", "targetMicroservice", "MESSAGING-SOCIAL-MICROSERVICE", "description", "Community Social Lunch Feed")
        );
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get real-time gateway routing metrics & request counts")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(gatewayMetricsService.getMetrics());
    }
}
