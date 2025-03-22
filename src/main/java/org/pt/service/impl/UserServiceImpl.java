package org.pt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.pt.dto.LoginDto;
import org.pt.components.Response;
import org.pt.exception.InvitationException;
import org.pt.exception.LoginException;
import org.pt.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.pt.config.Constants.IVCODE_EXPIRE_DAY;

@Slf4j
@Service
public class UserServiceImpl {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Response<Map<String, String>> login(LoginDto loginDto) throws LoginException {
        var map=new LinkedHashMap<String,String>();
        if(loginDto.getUsername().equals("admin")&&loginDto.getPassword().equals("admin")) {
            map.put("username", loginDto.getUsername());
            map.put("isAdmin","True");
            String token= JwtToken.create(map);
            map.put("token", token);
            return Response.success(map);
        }
        throw new LoginException("用户名不存在或密码错误");
    }


    public Response<Map<String, String>> getInvitationCode(String inviter,int remaining,String isAdmin) throws InvitationException {
        String inviterKey = "invitation:inviter:" + inviter;
        if (redisTemplate.hasKey(inviterKey)) {
            throw new InvitationException("邀请人已存在");
        }

        String invitationCode=String.valueOf(new Random().nextInt(899999)+100000);
        String invitationCodeKey = "invitationCode:" + invitationCode;

        while(redisTemplate.hasKey(invitationCodeKey))
        {
            //邀请码存在就重新生成
            invitationCode=String.valueOf(new Random().nextInt(899999)+100000);
            invitationCodeKey = "invitationCode:" + invitationCode;
        }
        // 存储邀请人与邀请码的映射关系
        redisTemplate.opsForValue().set(inviterKey, invitationCode);

        //管理员才可设置多次使用的邀请码
        if(!isAdmin.equals("True"))remaining=1;

        // 存储邀请码的详细信息
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        hashOps.put(invitationCodeKey, "inviter", inviter);
        hashOps.put(invitationCodeKey, "invitationCode", invitationCode);
        hashOps.put(invitationCodeKey, "remaining", remaining);

        // 设置过期时间
        redisTemplate.expire(invitationCodeKey, IVCODE_EXPIRE_DAY, TimeUnit.DAYS);
        redisTemplate.expire(invitationCodeKey, IVCODE_EXPIRE_DAY, TimeUnit.DAYS);

        var map=new LinkedHashMap<String,String>();
        map.put("inviter", inviter);
        map.put("remaining", String.valueOf(remaining));
        map.put("invitationCode", invitationCode);

        return Response.success(map);

    }
}
