package com.mozcalti.gamingapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MailMessageSourceConfiguration {
    @Bean
    public ResourceBundleMessageSource mailMessageSource() {

        var source = new ResourceBundleMessageSource();
        source.setBasenames("messages/mailMessages");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }
}
