package com.tbread.oauthhelper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.tbread.oauthhelper")
public class ConfigureOAuthHelper {

    @Bean
    public OAuth2RequestManager oAuth2RequestManager(){
        return new OAuth2RequestManager();
    }
}
