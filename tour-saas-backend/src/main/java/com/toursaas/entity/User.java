package com.toursaas.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TableField("username")
    @Column(unique = true, nullable = false)
    private String username;

    @TableField("password")
    @Column(nullable = false)
    private String password;

    @TableField("email")
    @Column(unique = true, nullable = false)
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("full_name")
    private String fullName;

    @TableField("role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    public enum UserRole {
        ADMIN, AGENT, USER
    }
}