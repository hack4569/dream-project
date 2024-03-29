package com.book.config;

import com.book.interceptor.RememberMeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rememberMeInterceptor());
    }

    @Bean
    public RememberMeInterceptor rememberMeInterceptor() {
        return new RememberMeInterceptor();
    }
    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogIntercepter())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add","/login","/css/**");
    }
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);;
        filterRegistrationBean.addUrlPatterns("/*");;
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean loginCheck(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);;
        filterRegistrationBean.addUrlPatterns("/*");;
        return filterRegistrationBean;
    }
    */
}
