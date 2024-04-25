package com.example.security1.oauth.provider;

import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public class NaverUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;  // oauth2User.getAttributes()

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return this.attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return this.attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return this.attributes.get("name").toString();
    }
}
