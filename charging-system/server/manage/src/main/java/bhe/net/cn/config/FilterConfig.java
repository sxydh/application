package bhe.net.cn.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bhe.net.cn.filter.CORSFilter;
import bhe.net.cn.filter.PreprocessFilter;

@Configuration
public class FilterConfig {

    static final Logger LOGGER = LoggerFactory.getLogger(FilterConfig.class);

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilter() {
        FilterRegistrationBean<CORSFilter> register = new FilterRegistrationBean<>();
        CORSFilter corsFilter = new CORSFilter();
        register.setFilter(corsFilter);
        register.setName(corsFilter.getClass().getSimpleName());
        register.setOrder(1);
        List<String> urlPatterns = new ArrayList<>(1);
        urlPatterns.add("/*");
        register.setUrlPatterns(urlPatterns);
        return register;
    }

    @Bean
    public FilterRegistrationBean<PreprocessFilter> preprocessFilter() {
        FilterRegistrationBean<PreprocessFilter> register = new FilterRegistrationBean<>();
        PreprocessFilter preprocessFilter = new PreprocessFilter();
        register.setFilter(preprocessFilter);
        register.setName(preprocessFilter.getClass().getSimpleName());
        register.setOrder(2);
        List<String> urlPatterns = new ArrayList<>(1);
        urlPatterns.add("/*");
        register.setUrlPatterns(urlPatterns);
        return register;
    }

}
