package bhe.net.cn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import bhe.net.cn.interceptor.VerifyInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private VerifyInterceptor verifyInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        LOGGER.info("addResourceHandlers(ResourceHandlerRegistry registry)");
        registry.addResourceHandler("/custom/**").addResourceLocations("classpath:/custom/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyInterceptor).addPathPatterns("/**");
    }

}
