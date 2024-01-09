package com.example.fusekianimalapp.common;

import com.example.fusekianimalapp.model.Triple;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestService {
    @Value("${fuseki.server}")
    private String fusekiServer;

    public List<Triple> getDataByAllFieldContains(String searchText){

        // Text input for filtering subjects

        // SPARQL query with a filter condition for the subject
        String sparqlQuery = "SELECT ?sub ?pred ?obj WHERE { " +
                "?sub ?pred ?obj . " +
                "FILTER(CONTAINS(STR(?sub), \"{1}\") || CONTAINS(STR(?sub), \"{1}\") || CONTAINS(STR(?sub), \"{1}\")) }";
        sparqlQuery = sparqlQuery.replace("{1}",searchText);

        List<Triple> tripples = new ArrayList<>();

        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            // Set up the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fusekiServer))
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
                    String subject = extractPartFromUrl(binding.path("sub").path("value").asText());
                    String predicate = extractPartFromUrl(binding.path("pred").path("value").asText());
                    String object = extractPartFromUrl(binding.path("obj").path("value").asText());

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

    public Triple getTripleDistinctBySubject(String sub){

        // Text input for filtering subjects

        // SPARQL query with a filter condition for the subject
        String sparqlQuery = "SELECT ?sub ?pred ?obj WHERE { " +
                "?sub ?pred ?obj . " +
                "FILTER( ?sub = <http://example.org/animal-ontology#{1}>)} LIMIT 1";
        sparqlQuery = sparqlQuery.replace("{1}",sub);


        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            // Set up the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fusekiServer))
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
                    String subject = extractPartFromUrl(binding.path("sub").path("value").asText());
                    String predicate = extractPartFromUrl(binding.path("pred").path("value").asText());
                    String object = extractPartFromUrl(binding.path("obj").path("value").asText());

                    return new Triple(subject, predicate, object);
                }
            } else {
                System.out.println("Error: " + response.statusCode() + ", " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private  String extractPartFromUrl(String subject) {
        String regex = "#(\\w+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(1); // Return the first capturing group
        }

        return subject; // Return null if no match is found
    }
}
