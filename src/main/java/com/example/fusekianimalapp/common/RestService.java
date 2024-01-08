package com.example.fusekianimalapp.common;

import com.example.fusekianimalapp.model.Triple;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestService {
    public List<Triple> getBySubject(String searchText){
        String fusekiEndpoint = "http://localhost:3030/ds/sparql";

        // Text input for filtering subjects

        // SPARQL query with a filter condition for the subject
        String sparqlQuery = "SELECT ?sub ?pred ?obj WHERE { " +
                "?sub ?pred ?obj . " +
                "FILTER(CONTAINS(STR(?sub), \"" + searchText + "\")) } LIMIT 10";

        List<Triple> tripples = new ArrayList<>();

        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            // Set up the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fusekiEndpoint))
                    .header("Content-Type", "application/sparql-query")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(sparqlQuery, StandardCharsets.UTF_8))
                    .build();

            // Send the request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Process the response
            if (response.statusCode() == 200) {
                System.out.println(response.body());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());

                JsonNode bindings = jsonNode.path("results").path("bindings");

                for (JsonNode binding : bindings) {
                    String subject = binding.path("sub").path("value").asText();
                    String predicate = binding.path("pred").path("value").asText();
                    String object = binding.path("obj").path("value").asText();

                    tripples.add(new Triple(subject, predicate, object));
                }
            } else {
                System.out.println("Error: " + response.statusCode() + ", " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripples;
    }
}
