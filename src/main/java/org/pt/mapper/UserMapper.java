package org.pt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.pt.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 可以自定义方法
}