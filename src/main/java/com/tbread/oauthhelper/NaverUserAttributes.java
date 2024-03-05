package com.tbread.oauthhelper;

import org.json.JSONObject;

public class NaverUserAttributes extends SocialUserAttributes {

    private final String username;
    private final String socialId;
    private final SocialProvider socialProvider;
    private final String name;

    public NaverUserAttributes(JSONObject json){
        this.username = json.getJSONObject("response").getString("email");
        this.socialId = json.getJSONObject("response").getString("id");
        this.socialProvider = SocialProvider.NAVER;
        this.name = json.getJSONObject("response").getString("name");
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
}
