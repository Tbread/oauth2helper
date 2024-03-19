package com.tbread.oauthhelper;

import org.json.JSONObject;

import java.util.Objects;

public class NaverUserAttributes extends SocialUserAttributes {

    private final String username;
    private final String socialId;
    private final SocialProvider socialProvider;
    private final String name;
    private final JSONObject attr;

    public NaverUserAttributes(JSONObject json){
        this.username = json.getJSONObject("response").getString("email");
        this.socialId = json.getJSONObject("response").getString("id");
        this.socialProvider = SocialProvider.NAVER;
        this.name = json.getJSONObject("response").has("name") ? json.getJSONObject("response").getString("name") : null;
        this.attr = json;
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject getAttr(){
        return attr;
    }

}
