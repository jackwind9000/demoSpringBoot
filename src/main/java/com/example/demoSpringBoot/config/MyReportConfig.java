package com.example.demoSpringBoot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MyReportConfig {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.US);
        return sessionLocaleResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("classpath:myReportTitle");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8"); //ISO-8859-1
        resourceBundleMessageSource.setCacheSeconds(300);
        //setUseCodeAsDefaultMessage: true: return default message if message code is not found. false: return null or an empty string if message code is not found
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        return resourceBundleMessageSource;
    }
}
