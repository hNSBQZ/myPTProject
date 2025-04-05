package org.pt.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pt.model.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String username;

    private String email;

    private Date registerTime;

    private String bio;

    private Integer age;

    private String school;

    private String college;

    private Long totalUpload;

    private Long totalDownload;

    private Integer inviteCount;

    private Integer magicValue;

    private Integer workCount;

}
