package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Order;
import com.example.springboot.mapper.OrderMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {


    @Resource
    OrderMapper orderMapper;


    //    数据库--新增操作
    @PostMapping
    public Result<?> save(@RequestBody Order order) {
        order.setCreateTime(new Date());
        orderMapper.insert(order);
        return Result.success();
    }

    //    数据库--查询操作
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Order::getUsername, search);
        }
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(orderPage);
    }

    //    数据库--修改（更新）操作
    @PutMapping
    public Result<?> update(@RequestBody Order order) {
        orderMapper.updateById(order);
        return Result.success();
    }

    //    数据库--删除操作
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        orderMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        orderMapper.deleteBatchIds(ids);
        return Result.success();
    }
}