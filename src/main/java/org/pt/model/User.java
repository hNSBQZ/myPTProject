package org.pt.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("users") // 指定表名
public class User {

    @TableId
    private Integer id;

    @TableField("username") // 用户ID
    private String username;

    @TableField("email") // 邮箱
    private String email;

    @TableField("password") // 密码
    private String password;

    @TableField("pass_key") // PassKey
    private String passKey;

    @TableField("is_admin")
    private Boolean isAdmin;

    @TableField("register_time") // 注册时间
    private Date registerTime;

    @TableField("bio") // 个人简介
    private String bio;

    @TableField("age") // 年龄
    private Integer age;

    @TableField("school") // 学校
    private String school;

    @TableField("college") // 学院
    private String college;

    @TableField("total_upload") // 总上传量
    private Long totalUpload;

    @TableField("total_download") // 总下载量
    private Long totalDownload;

    @TableField("invite_count") // 邀请数
    private Integer inviteCount;

    @TableField("magic_value") // 魔力值
    private Integer magicValue;

    @TableField("work_count") // 作品数
    private Integer workCount;
}
