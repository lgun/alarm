package com.example.gunialarm.classes;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
   
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webflow/css/**") // CSS 파일 요청 매핑 경로
                .addResourceLocations("/webflow/css/") // CSS 파일이 위치하는 폴더
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS));
                
        registry.addResourceHandler("/filePath/**") // 변경되어서 보여질 파일경로
                .addResourceLocations("file:C:/upload/"); // 실제 파일경로 : file:을 붙여서 외부파일 경로 표시 
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/**"); // 모든 경로에 대해 인터셉터 적용

        //번역
        registry.addInterceptor(localeChangeInterceptor()); 
    }
        
    //------번역 
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages");  // 기본 메시지 파일을 messages_en.properties로 설정하고, 다른 언어 파일을 지원
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREAN);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
    //------번역
}