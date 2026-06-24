package com.akhil.ai_notes_manager.service;
import com.akhil.ai_notes_manager.exception.AIServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIService {
    @Value("${groq.api.key}")
    private String apiKey;
    private final RestClient restClient;
    public AIService(){
        this.restClient=RestClient.builder().build();
    }
    public String generateSummary(String noteContent){
            String requestBody = """
            {
              "model": "llama-3.3-70b-versatile",
              "messages": [
                {
                  "role": "user",
                  "content": "Summarize this note in 3-5 concise sentences: %s"
                }
              ]
            }
            """.formatted(noteContent);

            String response = restClient.post()
                    .uri("https://api.groq.com/openai/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            ObjectMapper mapper = new ObjectMapper();

            try {
                JsonNode root = mapper.readTree(response);

                return root.get("choices")
                           .get(0)
                           .get("message")
                           .get("content")
                           .asText();

            }
            catch (Exception e) {

                throw new AIServiceException(
                    "Failed to parse AI response"
                );
            }
    }

}
