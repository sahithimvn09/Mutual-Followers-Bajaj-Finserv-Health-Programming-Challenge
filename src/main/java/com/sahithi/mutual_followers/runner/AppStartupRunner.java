package com.sahithi.mutual_followers.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sahithi.mutual_followers.model.User;
import com.sahithi.mutual_followers.model.WebhookResponse;
import com.sahithi.mutual_followers.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    public AppStartupRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Fetching webhook data...");

        // Option 1: Use real API
        WebhookResponse response = webhookService.fetchWebhookData();
        if (response == null || response.getData() == null) {
            System.err.println("Failed to fetch or parse webhook data. Check API response or mapping.");
            return;
        }
        List<User> users = response.getData().getUsers().getUsers();

        // Option 2: Use hardcoded test data (uncomment below to test locally)
//        List<User> users = getTestUsers();

        List<List<Integer>> outcome = webhookService.findMutualFollowers(users);

        // Create result map
        Map<String, Object> result = new HashMap<>();
        result.put("regNo", "AP22110011133");
        result.put("outcome", outcome);

        // Print as pretty JSON
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty print
            String jsonResult = objectMapper.writeValueAsString(result);

            System.out.println("\nFinal Output:");
            System.out.println(jsonResult);
        } catch (Exception e) {
            System.err.println("Failed to convert result to JSON");
            e.printStackTrace();
        }
    }

    // 👇 Hardcoded test users for local testing
    private List<User> getTestUsers() {
        List<User> users = new ArrayList<>();

        users.add(createUser(1, "Alice", Arrays.asList(2, 3)));
        users.add(createUser(2, "Bob", Arrays.asList(1)));
        users.add(createUser(3, "Charlie", Arrays.asList(4)));
        users.add(createUser(4, "David", Arrays.asList(3)));

        return users;
    }

    private User createUser(int id, String name, List<Integer> follows) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setFollows(follows);
        return user;
    }
}
