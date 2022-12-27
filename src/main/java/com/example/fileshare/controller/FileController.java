package com.example.fileshare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.fileshare.common.Result;
import com.example.fileshare.service.IFileService;
import com.example.fileshare.vo.EditVo;
import com.example.fileshare.vo.FileVo;
import com.example.fileshare.vo.ImgVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author vague 2022/4/29 14:32
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private final IFileService fileService;


    /**
     * 文件上传
     * @param file /
     * @param path /
     * @return /
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file, String path) {
        try {
            fileService.storeFile(file, path);
            return Result.success();
        }catch (IOException e){
            return Result.failure(e.getMessage());
        }
    }


    /**
     * 图片上传
     * @param file /
     * @return /
     */
    @PostMapping("/img/upload")
    public Result<ImgVo> uploadImg(MultipartFile file) {
        try {
            return Result.success(fileService.storeImgFile(file));
        }catch (IOException e){
            return Result.failure(e.getMessage());
        }

    }


    /**
     * 列出文件及文件夹
     * @param folderPath /
     * @return /
     */
    @GetMapping("/searchFolder")
    public Result<List<FileVo>> searchFolder(String folderPath){
        return Result.success(fileService.searchFolder(folderPath));
    }


    /**
     * 新建文件夹
     * @param filePath /
     * @param directoryName /
     * @return /
     */
    @GetMapping("/createDirectory")
    public Result<String> createDirectory(String filePath, String directoryName){
        return Result.success(fileService.createDirectory(filePath, directoryName));
    }


    /**
     * 文件预览
     * @param filePath /
     * @param response /
     */
    @GetMapping("/view")
    public void view(String filePath, HttpServletResponse response){
        fileService.view(filePath, response);
    }


    /**
     * 删除文件
     * @param fileVo /
     * @return /
     */
    @PostMapping("/removeFile")
    public Result<String> removeFile(@RequestBody FileVo fileVo){
        return Result.success(fileService.removeFile(fileVo));
    }


    /**
     * 保存文本修改
     * @param content /
     * @return /
     */
    @PostMapping("/saveEdit")
    public Result<String> saveEdit(String content){
        return Result.success(fileService.saveEdit(content));
    }


    /**
     * 获取文本
     * @return /
     */
    @GetMapping("/getEdit")
    public Result<EditVo> getEdit(){
        return Result.success(fileService.getEdit());
    }


    /**
     * 查询历史文本
     * @return /
     */
    @GetMapping("/listEdit")
    public Result<List<EditVo>> listEdit(){
        return Result.success(fileService.listEdit());
    }


    /**
     * 分页查询历史文本
     * @param page /
     * @param limit /
     * @return /
     */
    @GetMapping("/pageEdit")
    public Result<IPage<EditVo>> pageEdit(Long page, Long limit){
        return Result.success(fileService.pageEdit(page , limit));
    }


    /**
     * 删除文本记录
     * @param id /
     */
    @GetMapping("/remove/{id}")
    public void remove(@PathVariable String id){
        fileService.removeEdit(id);
    }

}
