package com.example.fileshare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.fileshare.vo.EditVo;
import com.example.fileshare.vo.FileVo;
import com.example.fileshare.vo.ImgVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author vague 2022/4/29 14:33
 */
public interface IFileService extends IService<FileVo> {

    /**
     * 文件夹
     */
    int TYPE_FOLDER = 1;


    /**
     * 文件
     */
    int TYPE_FILE = 2;


    /**
     * 列举文件及文件夹
     * @param folderPath 指定路径
     * @return 列表
     */
    List<FileVo> searchFolder(String folderPath);

    /**
     * 存储文件到指定的路径
     * @param file 文件
     * @param path 路径
     * @throws IOException io异常
     */
    void storeFile(MultipartFile file, String path) throws IOException;


    /**
     * 新建文件夹
     * @param filePath 文件路径
     * @param directoryName 文件夹名称
     * @return 执行结果
     */
    String createDirectory(String filePath, String directoryName);

    /**
     * 删除文件
     * @param fileVo 文件对象
     * @return 执行结果
     */
    String removeFile(FileVo fileVo);


    /**
     * 文件预览
     * @param filePath 路径
     * @param response 响应
     */
    void view(String filePath, HttpServletResponse response);


    /**
     * 存储图片
     * @param file 文件
     * @return 图片对象
     * @throws IOException io异常
     */
    ImgVo storeImgFile(MultipartFile file) throws IOException;

    /**
     * 保存共享文本
     * @param content 内容
     * @return /
     */
    int saveEdit(String content);

    /**
     * 获取编辑内容
     * @return /
     */
    EditVo getEdit();


    /**
     * 共享文本列表查询
     * @return /
     */
    List<EditVo> listEdit();

    /**
     * 共享文本分页查询
     * @return /
     */
    IPage<EditVo> pageEdit(Long page, Long limit);


    /**
     * 删除共享文本
     * @param id id
     */
    void removeEdit(String id);


}
