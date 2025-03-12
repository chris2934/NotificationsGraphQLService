package org.example;

import java.util.HashMap;
import java.util.Map;

public class GraphQLPayload {
    private String query;                 // The main GraphQL query/mutation
    private Map<String, Object> variables; // Variables for the query/mutation

    public GraphQLPayload(String query, Object input) {
        this.query = query;
        this.variables = new HashMap<>();
        this.variables.put("input", input); // Add the input to the variables map
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
