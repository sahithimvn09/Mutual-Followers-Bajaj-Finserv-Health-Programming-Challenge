package com.sahithi.mutual_followers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponse {
    private String webhook;
    private String accessToken;
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private UsersContainer users;

        public UsersContainer getUsers() {
            return users;
        }

        public void setUsers(UsersContainer users) {
            this.users = users;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UsersContainer {
        private List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
