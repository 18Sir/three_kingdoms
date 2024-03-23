package com.three_kingdoms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.exception.BusinessException;

import java.util.Date;
import java.util.Map;

public class JWTUtil {
    private JWTUtil(){

    }
    private static String key = "tktoken";
    public static String genToken(Map<String, Object> claims){
        return JWT.create().withClaim("user",claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .sign(Algorithm.HMAC256(key));
    }

    public static Map<String,Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(key)).
                build().
                verify(token).
                getClaim("user").
                asMap();
    }

    public static Long getTokenUid(String token){
        try {
            Map<String,Object> map = parseToken(token);
            return Long.valueOf(map.get("uid").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.BUSINESS_ERR,"数据不合法");
        }
    }
}
