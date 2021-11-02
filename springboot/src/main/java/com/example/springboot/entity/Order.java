package com.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("orders")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String category;
    private Integer productId;
    private BigDecimal price;
    private String username;
    private Integer userId;
    @JsonFormat(pattern="YYYY-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
