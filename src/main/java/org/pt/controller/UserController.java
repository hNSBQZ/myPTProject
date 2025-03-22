package org.pt.controller;

import lombok.extern.slf4j.Slf4j;
import org.pt.components.Response;
import org.pt.dto.LoginDto;
import org.pt.exception.InvitationException;
import org.pt.exception.LoginException;
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

    @PostMapping("login")
    public Response<Map<String, String>> login(@RequestBody LoginDto loginDto) throws LoginException {
        return userServiceImpl.login(loginDto);
    }

    @GetMapping("getInvitationCode")
    public Response<Map<String, String>> getInvitationCode(@RequestParam int remaining) throws InvitationException
    {
        String username=JwtToken.getUserInfo("username");
        String isAdmin=JwtToken.getUserInfo("isAdmin");
        return userServiceImpl.getInvitationCode(username,remaining,isAdmin);
    }

    @PostMapping("whoCame")
    public Response<Map<String, String>> whoCame(@RequestBody LoginDto loginDto) throws LoginException {
        var map=new LinkedHashMap<String,String>();
        map.put("username", JwtToken.getUserInfo("username"));
        return Response.success(map);
    }

}
