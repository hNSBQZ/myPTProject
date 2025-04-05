package org.pt.dto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterDto {

    private String username;

    private String email;

    private String password;

    private String bio;

    private Integer age;

    private String school;

    private String college;
}
