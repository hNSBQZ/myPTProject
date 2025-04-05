package org.pt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.pt.dto.LoginDto;
import org.pt.components.Response;
import org.pt.exception.InvitationException;
import org.pt.exception.LoginException;
import org.pt.exception.RegisterException;
import org.pt.mapper.UserMapper;
import org.pt.model.User;
import org.pt.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.pt.config.Constants.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Response<Map<String, String>>register(){
        var map=new LinkedHashMap<String,String>();
        return Response.success(map);
    }

    public Response<Map<String, String>> login(LoginDto loginDto) throws LoginException {
        //验证验证码
        Object obStoredCode= redisTemplate.opsForValue().get(loginDto.getEmail());
        if(obStoredCode==null){throw new LoginException("验证码过期");}
        String storedCode= (String) obStoredCode;
        if(!storedCode.equals(loginDto.getVerificationCode())){throw new LoginException("验证码错误");}

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){throw new LoginException("用户不存在");}

        String password=user.getPassword();
        String email=user.getEmail();
        if(!password.equals(loginDto.getPassword())){throw new LoginException("密码不正确");}
        if(!email.equals(loginDto.getEmail())){throw new LoginException("邮箱与用户名不匹配");}

        var map=new LinkedHashMap<String,String>();
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("isAdmin", String.valueOf(user.getIsAdmin()));
        String token = JwtToken.create(map);

        //用redis存储token，实现唯一登录
        redisTemplate.opsForValue().set(
                "user:latest_token:" + user.getUsername(),
                token
        );
        redisTemplate.expire("user:latest_token:" + user.getUsername(), JwtToken.AMOUNT, TimeUnit.HOURS);
        map.put("token", token);
        return Response.success(map);
    }

    public Response<Map<String,String>> getVerifyCode(String username,String emailAdd,int isLogin) throws LoginException, RegisterException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",emailAdd);
        User user = userMapper.selectOne(queryWrapper);
        //如果是登陆的话需要验证邮箱是否存在,用户名和邮箱是否匹配
        if(isLogin==1) {
            if(user==null) {throw new LoginException("没有该邮箱");}
            if(!user.getUsername().equals(username)) {throw new LoginException("邮箱和用户不匹配");}
        }
        else{
            //注册时邮箱不能存在
            if(user!=null){throw new RegisterException("邮箱已存在");}
        }

        String verifyCode =String.valueOf(new Random().nextInt(899999)+100000);

        redisTemplate.opsForValue().set(emailAdd, verifyCode);
        redisTemplate.expire(emailAdd, VECODE_EXPIRE_SECOND, TimeUnit.SECONDS);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_SENDER); // 发件人
        message.setTo(emailAdd); // 收件人
        message.setSubject("您的验证码"); // 主题
        message.setText("您的验证码是：" + verifyCode + "，有效期为1分钟。"); // 内容

        mailSender.send(message);

        var map=new LinkedHashMap<String,String>();
        map.put("email", emailAdd);
        map.put("verifyCode", verifyCode);
        return Response.success(map);
    }

    public Response<Map<String, String>> getInvitationCode(String inviter,int remaining,Boolean isAdmin) throws InvitationException {
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
        if(!isAdmin)remaining=1;

        // 存储邀请码的详细信息
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        hashOps.put(invitationCodeKey, "inviter", inviter);
        hashOps.put(invitationCodeKey, "invitationCode", invitationCode);
        hashOps.put(invitationCodeKey, "remaining", remaining);

        // 设置过期时间
        redisTemplate.expire(inviterKey, IVCODE_EXPIRE_DAY, TimeUnit.DAYS);
        redisTemplate.expire(invitationCodeKey, IVCODE_EXPIRE_DAY, TimeUnit.DAYS);

        var map=new LinkedHashMap<String,String>();
        map.put("inviter", inviter);
        map.put("remaining", String.valueOf(remaining));
        map.put("invitationCode", invitationCode);

        return Response.success(map);

    }

    public Response<List<User>> getUsers()
    {
        return Response.success(userMapper.selectList(null));
    }
}
