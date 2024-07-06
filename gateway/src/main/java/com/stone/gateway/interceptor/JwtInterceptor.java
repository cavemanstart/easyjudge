package com.stone.gateway.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stone.common.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头中传过来的token
        String token = request.getHeader("token");
        Map<String, Object> map = new HashMap<>();
        try {
            //校验token
            JwtUtils.verify(token);
            return true;// 放行 令牌正常才会执行到这里
        } catch (SignatureVerificationException e) {
            //无效签名
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            //过期
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            //算法不一致
            map.put("msg", "算法不一致");
        } catch (Exception e) {
            //token无效
            map.put("msg", "无效token...");
        }
        map.put("code", 501);

        // 把返回的map封装为JSON
        String json = new ObjectMapper().writeValueAsString(map);
        //设置响应类型和编码
        response.setContentType("application/json;charset=UTF-8");
        //返回响应
        response.getWriter().println(json);

        return false;
    }
}
