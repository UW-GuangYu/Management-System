package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Book;
import com.example.springboot.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/book")
public class BookController {


    @Resource
    BookMapper bookMapper;


    //    数据库--新增操作
    @PostMapping
    public Result<?> save(@RequestBody Book book) {
        if (book.getName() == null){
            book.setName("未命名");
        }
        bookMapper.insert(book);
        return Result.success();
    }

    //    数据库--查询操作
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Book::getName, search);
        }
        Page<Book> bookPage = bookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(bookPage);
    }

    //    数据库--修改（更新）操作
    @PutMapping
    public Result<?> update(@RequestBody Book book) {
        bookMapper.updateById(book);
        return Result.success();
    }

    //    数据库--删除操作
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        bookMapper.deleteById(id);
        return Result.success();
    }

}