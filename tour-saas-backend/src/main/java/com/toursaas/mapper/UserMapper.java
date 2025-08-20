package com.toursaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toursaas.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * User MyBatis-Plus Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus provides all CRUD operations automatically
    // Custom queries can be added here if needed
}