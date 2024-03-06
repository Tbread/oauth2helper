package com.tbread.oauthhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.tbread.oauthhelper")
@RequiredArgsConstructor
public class ConfigureOAuthHelper {

    private final Environment environment;

    @Bean
    public OAuth2RequestManager oAuth2RequestManager() {
        return new OAuth2RequestManager(
                environment.getProperty("oauth2.helper.callback.naver"),
                environment.getProperty("oauth2.helper.key.naver"),
                environment.getProperty("oauth2.helper.secret.naver"),
                environment.getProperty("oauth2.helper.callback.kakao"),
                environment.getProperty("oauth2.helper.key.kakao"),
                environment.getProperty("oauth2.helper.response-type.naver", "code"),
                environment.getProperty("oauth2.helper.response-type.kakao", "code"),
                environment.getProperty("oauth2.helper.grant-type.naver", "authorization_code"),
                environment.getProperty("oauth2.helper.grant-type.kakao", "authorization_code"),
                environment.getProperty("oauth2.helper.scope.naver", "email profile"),
                environment.getProperty("oauth2.helper.scope.kakao", "email profile"));
    }
}
