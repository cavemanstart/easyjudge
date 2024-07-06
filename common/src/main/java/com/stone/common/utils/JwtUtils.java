package com.stone.common.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Calendar;

public class JwtUtils {
    //token超时变量，N（秒）后
    public static final int TOKEN_TIMEOUT = 60*60*1;
    //  密钥
    public static final String APP_SECRET = "xxx@#$%^&dong";

    //生成token方法:传入用户名、用户ID 作为payload写入，便于后期解析
    public static String createToken(long id,String name) {
        Calendar instance=Calendar.getInstance();
        instance.add(Calendar.SECOND,TOKEN_TIMEOUT);
        String token=JWT.create()
                .withClaim("uid",id)        //payload，写入变量，用户ID
                .withClaim("uname",name)  //payload，写入变量，用户名
                .withExpiresAt(instance.getTime())   //设置过期
                .sign(Algorithm.HMAC256(APP_SECRET));//签名及算法
        return token;
    }

    //验证token合法性
    public static DecodedJWT verify(String token){
        // 此处如果没有出异常，说明传入token合法且未过期
        DecodedJWT verify=JWT.require(Algorithm.HMAC256(APP_SECRET)).build().verify(token);
        return verify;
    }

    public static DecodedJWT getTokenInfo(String token){

        try {
            DecodedJWT verify=JWT.require(Algorithm.HMAC256(APP_SECRET)).build().verify(token);
            return verify;
        }catch (Exception e){
            return null;
        }
    }

}
