package org.pt.components.interceptor;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.pt.components.MsgEnum;
import org.pt.components.Response;
import org.pt.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JwtToken.TOKEN_HEADER);
        String msg;
        ServletOutputStream os = response.getOutputStream();
        String uri = request.getRequestURI();

        try {
            JwtToken.verify(token);
            String username = JwtToken.getUsername(token);
            String storedToken=redisTemplate.opsForValue().get(
                    "user:latest_token:" + username
            );
            //登陆过期
            if(storedToken==null){os.write(JSON.toJSONString(Response.error(MsgEnum.TOKEN_EXPIRE.getMessage(), MsgEnum.TOKEN_EXPIRE.getCode())).getBytes());return false;}
            if(!storedToken.equals(token)){os.write(JSON.toJSONString(Response.error(MsgEnum.TOKEN_USED.getMessage(), MsgEnum.TOKEN_USED.getCode())).getBytes());return false;}
            log.info("in token interceptor, this url is {}, valid", uri);
            return true;
        } catch (TokenExpiredException e) {
            log.error("in token interceptor, this url is {}, expired", uri);
            os.write(JSON.toJSONString(Response.error(MsgEnum.TOKEN_EXPIRE.getMessage(), MsgEnum.TOKEN_EXPIRE.getCode())).getBytes());
        } catch (Exception e) {
            log.error("in token interceptor, this uri is {}, invalid token", uri);
            log.error(e.toString());
            os.write(JSON.toJSONString(Response.error(MsgEnum.TOKEN_ERROR.getMessage(), MsgEnum.TOKEN_ERROR.getCode())).getBytes());
        }

        return false;
    }
}
