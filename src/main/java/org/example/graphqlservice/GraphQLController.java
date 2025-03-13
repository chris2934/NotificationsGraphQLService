package org.example.graphqlservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.SendMessageInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/graphql-proxy")
@Validated
public class GraphQLController {

    @Value("${graphql.endpoint}")
    private String graphqlEndpoint;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GraphQLController.class);

    @Autowired
    public GraphQLController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody SendMessageInput input) {
        try {
            // Construct the GraphQL Mutation query as a string
            String mutation = "mutation sendMessage($input: SendMessageInput!) {" +
                    "  sendMessage(input: $input) {" +
                    "    id" +
                    "    status" +
                    "    timestamp" +
                    "  }" +
                    "}";

            // Prepare JSON payload using ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> variables = Map.of("input", input);
            Map<String, Object> payload = Map.of("query", mutation, "variables", variables);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + getAuthToken()); // Replace with a valid token mechanism

            // Create HTTP entity
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // Sending request using RestTemplate
            logger.info("Sending request to GraphQL endpoint: {}", graphqlEndpoint);
            ResponseEntity<String> response = restTemplate.exchange(
                    graphqlEndpoint,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Log and return the response
            logger.info("Received response from GraphQL endpoint: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            // Handle client-side errors
            logger.error("Client error: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getStatusCode())
                    .body("Client Error: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            // Handle server-side errors
            logger.error("Server error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Server Error: Unable to process the request.");
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments passed to the method
            logger.error("Invalid argument provided: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid argument: " + e.getMessage());
        } catch (JsonProcessingException e) {
            // Handle JSON processing errors
            logger.error("JSON Processing error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("JSON Error: Invalid payload.");
        } catch (Exception e) {
            // Catch any other unexpected errors
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves an authorization token. Replace this with a
     * proper token generation or retrieval logic.
     */
    private String getAuthToken() {
        // Example static token. Replace this with a dynamic mechanism.
        return "example-static-token";
    }
}