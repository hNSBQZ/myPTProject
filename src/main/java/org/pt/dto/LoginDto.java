package org.pt.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String verificationCode;
    private String email;
}
