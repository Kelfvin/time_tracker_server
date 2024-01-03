package com.kelf.spring_boot.config;


import com.kelf.spring_boot.inerceptor.LoginInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.windows.path}")
    String windowsUploadPath;

    @Value("${upload.linux.path}")
    String linuxUploadPath;

    // 注册登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login");//除了/user/login路径以外都拦截
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 判断是Windows还是Linux系统
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            // Windows系统
            registry.addResourceHandler("/file/**").addResourceLocations("file:" + windowsUploadPath+"/");
        } else {
            // Linux系统
            registry.addResourceHandler("/file/**").addResourceLocations("file:"+linuxUploadPath+"/");
        }
    }


}
