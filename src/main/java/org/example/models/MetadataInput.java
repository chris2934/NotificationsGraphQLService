package org.example.models;

public class MetadataInput {
    private String type;    // The type field (required)
    private String version; // The version field (required)

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
