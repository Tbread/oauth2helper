package com.tbread.oauthhelper;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuth2RequestManager {

    private final String naverCallbackUrl;
    private final String naverKey;
    private final String naverSecretKey;
    private final String naverResponseType;
    private final String naverGrantType;
    private final String kakaoCallbackUrl;
    private final String kakaoKey;
    private final String kakaoResponseType;
    private final String kakaoGrantType;
    private final String naverScope;
    private final String kakaoScope;

    public OAuth2RequestManager(String naverCallbackUrl,
                                String naverKey,
                                String naverSecretKey,
                                String kakaoCallbackUrl,
                                String kakaoKey,
                                String naverResponseType,
                                String kakaoResponseType,
                                String naverGrantType,
                                String kakaoGrantType,
                                String naverScope,
                                String kakaoScope) {
        this.naverCallbackUrl = naverCallbackUrl;
        this.kakaoCallbackUrl = kakaoCallbackUrl;
        this.naverKey = naverKey;
        this.kakaoKey = kakaoKey;
        this.naverSecretKey = naverSecretKey;
        this.naverResponseType = naverCallbackUrl;
        this.kakaoResponseType = kakaoResponseType;
        this.naverGrantType = naverGrantType;
        this.kakaoGrantType = kakaoGrantType;
        this.naverScope = naverScope;
        this.kakaoScope = kakaoScope;
    }

    public SocialUserAttributes socialLogin(SocialProvider provider, String code) {
        SocialUserAttributes attributes = null;
        if (provider == SocialProvider.KAKAO) {
            JSONObject resJson = getKakaoUserInfoByAccessToken(getKakaoAccessToken(code));
            attributes = SocialUserFactory.getUserAttributes(SocialProvider.KAKAO, resJson);
        }
        if (provider == SocialProvider.NAVER) {
            JSONObject resJson = getNaverUserInfoByAccessToken(getNaverAccessToken(code));
            attributes = SocialUserFactory.getUserAttributes(SocialProvider.NAVER, resJson);
        }
        return attributes;
    }

    private String getNaverAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.postForEntity("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" + naverKey
                + "&client_secret=" + naverSecretKey
                + "&redirect_uri=" + naverCallbackUrl
                + "&code=" + code, request, String.class);
        JSONObject resJson = new JSONObject(response.getBody());
        if (!resJson.has("access_token")) {
            throw new RuntimeException("Access Token 발급에 실패했습니다.");
        }
        return resJson.getString("access_token");
    }

    private String getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        JSONObject body = new JSONObject();
        body.put("grant_type", "authorization_code");
        body.put("client_id", kakaoKey);
        body.put("redirect_uri", kakaoCallbackUrl);
        body.put("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = rt.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);
        JSONObject resJson = new JSONObject(response.getBody());
        if (!resJson.has("access_token")) {
            throw new RuntimeException("Access Token 발급에 실패했습니다.");
        }
        return resJson.getString("access_token");
    }

    private JSONObject getNaverUserInfoByAccessToken(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.getForEntity("https://openapi.naver.com/v1/nid/me", String.class, request);
        return new JSONObject(response.getBody());
    }

    private JSONObject getKakaoUserInfoByAccessToken(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.getForEntity("https://kapi.kakao.com/v2/user/me", String.class, request);
        return new JSONObject(response.getBody());
    }


    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?&client_id=" + kakaoKey
                + "&redirect_uri=" + kakaoCallbackUrl + "&scope=" + kakaoScope + "&response_type=" + kakaoResponseType;
    }

    public String getNaverLoginUrl() {
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + naverKey
                + "&redirect_uri=" + naverCallbackUrl + "&scope=" + naverScope + "&response_type=" + naverResponseType;
    }
}
