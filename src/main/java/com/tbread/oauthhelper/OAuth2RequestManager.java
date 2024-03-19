package com.tbread.oauthhelper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuth2RequestManager {

    private String naverCallbackUrl;
    private String naverKey;
    private String naverSecretKey;
    private String naverResponseType;
    private String naverGrantType;
    private String kakaoCallbackUrl;
    private String kakaoKey;
    private String kakaoResponseType;
    private String kakaoGrantType;
    private String naverScope;
    private String kakaoScope;
    private final Environment environment;

    public OAuth2RequestManager(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    void init() {
        this.naverCallbackUrl = environment.getProperty("oauth2.helper.callback.naver");
        this.naverKey = environment.getProperty("oauth2.helper.key.naver");
        this.naverSecretKey = environment.getProperty("oauth2.helper.secret.naver");
        this.kakaoCallbackUrl = environment.getProperty("oauth2.helper.callback.kakao");
        this.kakaoKey = environment.getProperty("oauth2.helper.key.kakao");
        this.naverResponseType = environment.getProperty("oauth2.helper.response-type.naver", "code");
        this.kakaoResponseType = environment.getProperty("oauth2.helper.response-type.kakao", "code");
        this.naverGrantType = environment.getProperty("oauth2.helper.grant-type.naver", "authorization_code");
        this.kakaoGrantType = environment.getProperty("oauth2.helper.grant-type.kakao", "authorization_code");
        this.naverScope = environment.getProperty("oauth2.helper.scope.naver", "email profile");
        this.kakaoScope = environment.getProperty("oauth2.helper.scope.kakao", "email profile");
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept","application/json");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoKey);
        params.add("redirect_uri", kakaoCallbackUrl);
        params.add("code", code);
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,request, String.class);
        JSONObject resJson = new JSONObject(response.getBody());
        if (!resJson.has("access_token")) {
            throw new RuntimeException("Access Token 발급에 실패했습니다.");
        }
        return resJson.getString("access_token");
    }

    private JSONObject getNaverUserInfoByAccessToken(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.getForEntity("https://openapi.naver.com/v1/nid/me", String.class, request);
        return new JSONObject(response.getBody());
    }

    private JSONObject getKakaoUserInfoByAccessToken(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        headers.add("Authorization","Bearer "+token);
        headers.add("Accept","application/json");
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,request,String.class);
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
