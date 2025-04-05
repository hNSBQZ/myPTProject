package org.pt.controller;

import lombok.extern.slf4j.Slf4j;
import org.pt.components.Response;
import org.pt.dto.LoginDto;
import org.pt.exception.InvitationException;
import org.pt.exception.LoginException;
import org.pt.exception.RegisterException;
import org.pt.model.User;
import org.pt.service.impl.UserServiceImpl;
import org.pt.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @GetMapping("/test")
    public Response<List<User>> getUsers() {
        return userServiceImpl.getUsers();
    }

    @GetMapping("verifyCode")
    public Response<Map<String,String>> verifyCode(@RequestParam String username,@RequestParam String emailAddr,@RequestParam int isLogin) throws LoginException, RegisterException {
        return userServiceImpl.getVerifyCode(username,emailAddr,isLogin);
    }

    @PostMapping("login")
    public Response<Map<String, String>> login(@RequestBody LoginDto loginDto) throws LoginException {
        return userServiceImpl.login(loginDto);
    }

    @GetMapping("getInvitationCode")
    public Response<Map<String, String>> getInvitationCode(@RequestParam String token, @RequestParam int remaining) throws InvitationException
    {
        String username=JwtToken.getUsername(token);
        Boolean isAdmin=JwtToken.isAdmin(token);
        return userServiceImpl.getInvitationCode(username,remaining,isAdmin);
    }


}
