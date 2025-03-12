package org.example.models;

public class SendMessageInput {
    private String content;         // The content field (required)
    private MetadataInput metadata; // MetadataInput field (required)
    private String status;          // The status field (required)
    private String timestamp;       // Timestamp field (required)

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MetadataInput getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataInput metadata) {
        this.metadata = metadata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
