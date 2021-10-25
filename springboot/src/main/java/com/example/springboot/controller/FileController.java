package com.example.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.springboot.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

//    文件上传功能接口（接受前端发来的文件）
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();       //获取文件名称
//        定义文件的唯一标识（前缀）IdUtil
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag + "_" + originalFilename; //获取上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);   //用工具集将文件存入（写入）指定路径
        return Result.success(ip + ":" + port + "/files/" + flag);        //返回结果 url
    }

    //    文件上传功能接口（接受wangEditor发来的图片   富文本文件上传接口）
    @PostMapping("/editor/upload")
    public JSONObject editorUpload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();       //获取文件名称
//        定义文件的唯一标识（前缀）IdUtil
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag + "_" + originalFilename; //获取上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);   //用工具集将文件存入（写入）指定路径
        String url = ip + ":" + port + "/files/" + flag;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        return json;        //返回结果 url
    }

//    文件下载接口
    @GetMapping("/{flag}")
    public void getFiles(HttpServletResponse response, @PathVariable String flag){
        OutputStream os;        //新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";     //定义文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames((basePath));        //获取所有的文件名称
        String filename = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  //找到和参数一致的文件
        try{
            if (StrUtil.isNotEmpty(filename)){
//                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
//                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + filename);     //通过文件路径读取文件字节流
                os = response.getOutputStream();        //通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        }
        catch (Exception e){
            System.out.println("文件下载失败");
        }
    }
}