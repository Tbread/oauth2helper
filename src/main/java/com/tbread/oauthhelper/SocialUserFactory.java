package com.tbread.oauthhelper;

import org.json.JSONObject;

public class SocialUserFactory {
    public static SocialUserAttributes getUserAttributes(SocialProvider provider, JSONObject json){
        if (provider == SocialProvider.KAKAO){
            return new KakaoUserAttributes(json);
        }
        if (provider == SocialProvider.NAVER){
            return new NaverUserAttributes(json);
        }
        return null;
    }
}
