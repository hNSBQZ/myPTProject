package org.pt.controller;

import lombok.extern.slf4j.Slf4j;
import org.pt.components.Response;
import org.pt.dto.*;
import org.pt.exception.InvitationException;
import org.pt.exception.LoginException;
import org.pt.exception.RegisterException;
import org.pt.exception.UserException;
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


    @GetMapping("getUserInfo")
    public Response<UserInfoDto> getUsers(@RequestParam String token, @RequestParam String username) throws UserException {
        return userServiceImpl.getUserInfo(username);
    }

    @GetMapping("verifyCode")
    public Response<Map<String,String>> verifyCode(@RequestParam String username,@RequestParam String emailAddr,@RequestParam int isLogin) throws LoginException, RegisterException {
        return userServiceImpl.getVerifyCode(username,emailAddr,isLogin);
    }

    @PostMapping("login")
    public Response<LoginResponseDto> login(@RequestBody LoginDto loginDto) throws LoginException {
        return userServiceImpl.login(loginDto);
    }

    @GetMapping("getInvitationCode")
    public Response<InvitationDto> getInvitationCode(@RequestParam String token, @RequestParam int remaining) throws InvitationException
    {
        Integer id=JwtToken.getId(token);
        Boolean isAdmin=JwtToken.isAdmin(token);
        return userServiceImpl.getInvitationCode(id,remaining,isAdmin);
    }

    @PostMapping("updateInfo")
    public Response<List<String>> updateInfo(@RequestBody UpdateInfoDto updateInfoDto) throws UserException {
        return userServiceImpl.updateUserInfo(updateInfoDto);
    }


}
