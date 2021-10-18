package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    UserMapper userMapper;


//    数据库--新增操作
    @PostMapping
    public Result<?> save(@RequestBody User user){
        if (user.getPassword() == null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

//    数据库--查询操作
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(User::getNickName, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

//    数据库--修改（更新）操作
    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

//    数据库--删除操作
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);
        return Result.success();
    }
}
