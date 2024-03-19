package com.tbread.oauthhelper;

import org.json.JSONObject;

import java.util.Objects;

public class KakaoUserAttributes extends SocialUserAttributes {

    private final String username;
    private final String socialId;
    private final SocialProvider socialProvider;
    private final String name;

    private final JSONObject attr;
    public KakaoUserAttributes(JSONObject json) {
        this.username = json.getJSONObject("kakao_account").getString("email");
        this.socialId = String.valueOf(json.getLong("id"));
        this.socialProvider = SocialProvider.KAKAO;
        this.name = json.getJSONObject("kakao_account").has("name") ? json.getJSONObject("kakao_account").getString("name") : null;
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
