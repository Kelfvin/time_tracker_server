package com.kelf.spring_boot.inerceptor;



import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前会话
        HttpSession session = request.getSession();

        // 判断用户是否已登录
        if (session.getAttribute("user") == null) {
            // 用户未登录，重定向到登录页面
            response.sendRedirect("/user/login");
            return false; // 拦截该请求，中止后续处理
        }

        return true; // 放行该请求，继续后续处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完成之后进行资源清理
        System.out.println("After-completion logic");
    }
}
