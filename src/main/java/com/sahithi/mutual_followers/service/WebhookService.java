package com.sahithi.mutual_followers.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahithi.mutual_followers.model.User;
import com.sahithi.mutual_followers.model.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();
    public WebhookResponse fetchWebhookData() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";

        Map<String, String> request = new HashMap<>();
        request.put("name", "M. V. N Sahithi");
        request.put("regNo", "AP22110011133");
        request.put("email", "venkatanagasahithi_mukkamala@srmap.edu.in");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        // Step 1: Get raw JSON
        ResponseEntity<String> rawResponse = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        System.out.println("RAW JSON RESPONSE:\n" + rawResponse.getBody());

        // Step 2: Convert raw JSON into WebhookResponse using Jackson
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WebhookResponse webhookResponse = objectMapper.readValue(rawResponse.getBody(), WebhookResponse.class);

            // Print parsed user data
            List<User> users = webhookResponse.getData().getUsers().getUsers();
            System.out.println("Users received from API:");
            for (User user : users) {
                System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Follows: " + user.getFollows());
            }

            return webhookResponse;

        } catch (Exception e) {
            System.err.println("Failed to parse JSON to WebhookResponse");
            e.printStackTrace();
            return null;
        }
    }

    public List<List<Integer>> findMutualFollowers(List<User> users) {
        Map<Integer, Set<Integer>> followMap = new HashMap<>();
        for (User user : users) {
            followMap.put(user.getId(), new HashSet<>(user.getFollows()));
        }

        Set<String> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (User user : users) {
            int id = user.getId();
            for (int followedId : user.getFollows()) {
                if (followMap.containsKey(followedId) &&
                        followMap.get(followedId).contains(id)) {
                    int min = Math.min(id, followedId);
                    int max = Math.max(id, followedId);
                    String key = min + "-" + max;
                    if (!seen.contains(key)) {
                        result.add(Arrays.asList(min, max));
                        seen.add(key);
                    }
                }
            }
        }

        return result;
    }
}
