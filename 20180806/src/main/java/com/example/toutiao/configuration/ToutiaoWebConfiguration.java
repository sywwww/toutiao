package com.example.toutiao.configuration;

import com.example.toutiao.intercept.LoginRequiredInterceptor;
import com.example.toutiao.intercept.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        //首先，得要有一个/setting的url，然后如果在登录态的话，正常显示/setting，如果没有登录用户，就跳转至拦截器设置的页面 httpServletResponse.sendRedirect("/index");
        super.addInterceptors(registry);
    }
}
