package com.toursaas.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toursaas.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository - 同时支持JPA和MyBatis-Plus
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JPA方法
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
    // MyBatis-Plus功能通过单独的Mapper提供
}