package com.smartnutrition.service;

import com.smartnutrition.dto.response.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AssistantService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @SuppressWarnings("unchecked")
    public ChatResponse getAIRecommendation(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            return getMockRecommendation(prompt);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                    "parts", List.of(Map.of(
                        "text", "You are a professional pediatric nutritionist assistant for the Smart Nutrition App. Answer this parent query in a concise, friendly, and practical way, suggesting healthy recipes or nutrition adjustments: " + prompt
                    ))
                ))
            );

            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        return new ChatResponse(text);
                    }
                }
            }
        } catch (Exception e) {
            // Fallback to mock on network or parsing error
            System.err.println("Gemini API error: " + e.getMessage() + ". Falling back to mock assistant.");
        }

        return getMockRecommendation(prompt);
    }

    private ChatResponse getMockRecommendation(String prompt) {
        String query = prompt.toLowerCase();
        String responseText;

        if (query.contains("protein")) {
            responseText = "🥦 **[Mock AI Assistant]** To boost your child's protein intake, try packing Paneer Bhurji with Roti, Chickpea Salad, Boiled Eggs, or Peanut Butter Sandwiches. These are easy to eat and keep them full throughout school hours!";
        } else if (query.contains("fiber") || query.contains("digestion") || query.contains("constipation")) {
            responseText = "🍎 **[Mock AI Assistant]** For higher fiber, try adding Apple slices, Roasted Makhana, Oats Upma, or Vegetable Pulao with carrots and peas to their lunchbox. Fiber is crucial for healthy digestion!";
        } else if (query.contains("allergy") || query.contains("allerg")) {
            responseText = "⚠️ **[Mock AI Assistant]** When packing for children with allergies (like peanuts or dairy), focus on safe alternatives like sunflower seed butter instead of peanut butter, and fruit cups or roasted chickpeas for quick snacks. Always verify with the teacher!";
        } else if (query.contains("recipe") || query.contains("cook") || query.contains("idea")) {
            responseText = "🍱 **[Mock AI Assistant]** Here is a quick lunchbox recipe suggestion:\n\n**Tofu/Paneer Veggie Roll**\n1. Sauté paneer cubes with bell peppers and mild spices.\n2. Spread green chutney on a whole wheat roti.\n3. Wrap the paneer mixture inside the roti. Wrap in foil.\n*This provides a perfect balance of carbs, protein, and dietary fiber!*";
        } else {
            responseText = "✨ **[Mock AI Assistant]** That sounds like a great question! For a balanced school lunch, aim for the 1/3 rule: 1 portion of complex carbs (roti/pulao), 1 portion of protein (paneer/dal/chicken), and plenty of colorful fruits or vegetables as sides. Let me know if you need specific recipe ideas!";
        }

        return new ChatResponse(responseText);
    }
}
