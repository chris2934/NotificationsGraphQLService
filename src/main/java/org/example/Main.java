package org.example;

import org.example.models.MetadataInput;
import org.example.models.SendMessageInput;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Create MetadataInput for the SendMessageInput
            MetadataInput metadata = new MetadataInput();
            metadata.setType("Alert");
            metadata.setVersion("1.0");

            // Step 2: Create SendMessageInput
            SendMessageInput input = new SendMessageInput();
            input.setContent("This is a test message.");
            input.setMetadata(metadata);
            input.setStatus("SENT");
            input.setTimestamp("2023-10-24T15:45:00Z");

            // Step 3: Call the service to send the message
            String response = AppSyncService.sendMessage(input);

            // Print the GraphQL response
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}