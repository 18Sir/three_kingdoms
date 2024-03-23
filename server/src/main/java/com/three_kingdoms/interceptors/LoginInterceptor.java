package com.three_kingdoms.interceptors;

import com.three_kingdoms.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String a = request.getHeader("Authorization");
        try {
            Map<String,Object> claims = JWTUtil.parseToken(a);
            return true;
        }catch (Exception ex){
            response.setStatus(401);
            return false;
        }
    }
}
