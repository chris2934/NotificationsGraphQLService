package org.example;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.models.SendMessageInput;

public class AppSyncService {

    private static final String GRAPHQL_URL = "https://ztjvnzn4pvddjmiufzjlhs7rhi.appsync-api.us-east-1.amazonaws.com/graphql";
    private static final String API_KEY = "da2-u224gqsjhbgjdgc7lh4ndgao34";

    public static String sendMessage(SendMessageInput input) throws Exception {
        // Step 1: Build the GraphQL Mutation Query
        String mutationTemplate = "mutation SendMessage($input: SendMessageInput!) { " +
                "sendMessage(input: $input) { " +
                "MessageId isRead MessageBody { content status timestamp metadata { type version } } " +
                "ReceivedAt } }";

        // Step 2: Create the GraphQL Payload with Variables
        Gson gson = new Gson();
        String payload = gson.toJson(new GraphQLPayload(mutationTemplate, input));

        // Step 3: Create HTTP Request
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(GRAPHQL_URL);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("x-api-key", API_KEY);

            StringEntity entity = new StringEntity(payload, "UTF-8");
            post.setEntity(entity);

            // Step 4: Execute the Request
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode >= 200 && statusCode < 300) {
                    return responseBody; // Return successful response
                } else {
                    throw new RuntimeException("GraphQL error: " + responseBody);
                }
            }
        }
    }
}