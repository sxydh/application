package bhe.net.cn.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bhe.net.cn.filter.AttrFilter;
import bhe.net.cn.filter.CORSFilter;
import bhe.net.cn.filter.GateFilter;

@Configuration
public class FilterConfig {

    static final Logger LOGGER = LoggerFactory.getLogger(FilterConfig.class);

    @Bean
    public FilterRegistrationBean<GateFilter> addGateFilter(GateFilter gateFilter) {
        FilterRegistrationBean<GateFilter> register = new FilterRegistrationBean<>();
        register.setFilter(gateFilter);
        register.setName(gateFilter.getClass().getSimpleName());
        register.setOrder(0);
        List<String> urlPatterns = new ArrayList<>(1);
        urlPatterns.add("/*");
        register.setUrlPatterns(urlPatterns);
        return register;
    }

    @Bean
    public FilterRegistrationBean<AttrFilter> addAttrFilter(AttrFilter attrFilter) {
        FilterRegistrationBean<AttrFilter> register = new FilterRegistrationBean<>();
        register.setFilter(attrFilter);
        register.setName(attrFilter.getClass().getSimpleName());
        register.setOrder(1);
        List<String> urlPatterns = new ArrayList<>(1);
        urlPatterns.add("/*");
        register.setUrlPatterns(urlPatterns);
        return register;
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> addCORSFilter(CORSFilter corsFilter) {
        FilterRegistrationBean<CORSFilter> register = new FilterRegistrationBean<>();
        register.setFilter(corsFilter);
        register.setName(corsFilter.getClass().getSimpleName());
        register.setOrder(2);
        List<String> urlPatterns = new ArrayList<>(1);
        urlPatterns.add("/*");
        register.setUrlPatterns(urlPatterns);
        return register;
    }

}
