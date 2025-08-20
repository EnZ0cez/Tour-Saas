package com.toursaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toursaas.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * Product MyBatis-Plus Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // MyBatis-Plus provides all CRUD operations automatically
    // Custom queries can be added here if needed
}