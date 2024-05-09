package com.three_kingdoms.interceptors;

import com.three_kingdoms.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
     private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        try {
            Map<String,Object> claims = JWTUtil.parseToken(token);
            Long uid = JWTUtil.getTokenUid(token);
            String key = "three-kingdoms:user:"+uid+":token";
            String redisToken = redisTemplate.opsForValue().get(key).toString();

            if(redisToken.equals(token)){
                return true;
            }else {
                response.setStatus(401);
                return false;
            }
        }catch (Exception ex){
            response.setStatus(401);
            return false;
        }
    }
}
