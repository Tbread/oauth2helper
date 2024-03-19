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
        return new OAuth2RequestManager(environment);
    }
}
