package com.example.fileshare.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fileshare.common.BizException;
import com.example.fileshare.mapper.EditMapper;
import com.example.fileshare.mapper.FileMapper;
import com.example.fileshare.service.IFileService;
import com.example.fileshare.vo.EditVo;
import com.example.fileshare.vo.FileVo;
import com.example.fileshare.vo.ImgVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vague 2022/4/29 14:34
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileVo> implements IFileService {

    @Value("${file-share.file-path:#{null}}")
    private String fileRootPath;

    private final EditMapper editMapper;

    @Override
    public List<FileVo> searchFolder(String folderPath) {
        File file;
        final String filePath;
        if (StringUtils.hasText(folderPath)) {
            filePath = folderPath;
            file = new File(filePath);
        } else {
            filePath = this.getFilePath();
            file = new File(filePath);
            if (!file.exists() && !file.mkdir()) {
                log.error("mkdir error");
            }
        }
        File[] files = file.listFiles();
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }
        List<FileVo> res = new ArrayList<>();
        for (File tFile : files) {
            FileVo fileVo = new FileVo();
            fileVo.setFileName(tFile.getName());
            if (tFile.isDirectory()) {
                fileVo.setType(TYPE_FOLDER);
                fileVo.setFileSize("-");
            } else {
                if (tFile.length() / 1024 < 1) {
                    fileVo.setFileSize("< 1kB");
                } else {
                    String size = String.format("%.2f", (double) tFile.length() / 1024 / 1024);
                    fileVo.setFileSize(size + "MB");
                }
                fileVo.setType(TYPE_FILE);
            }
            fileVo.setFilePath(filePath);
            fileVo.setCreateName("admin");
            if (!StringUtils.hasText(fileVo.getCreateTime())) {
                fileVo.setCreateTime(getFileCreateTime(fileVo.getFilePath() + File.separator + fileVo.getFileName()));
            }
            res.add(fileVo);
        }
        return res.stream().sorted(Comparator.comparing(FileVo::getCreateTime).reversed()).sorted(Comparator.comparing(FileVo::getType)).collect(Collectors.toList());
    }

    @Override
    public void storeFile(MultipartFile file, String targetPath) throws IOException {
        if (!StringUtils.hasText(targetPath)) {
            targetPath = this.getFilePath();
        } else {
            targetPath = targetPath.replace("\\", File.separator);
        }
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            filename = UUID.randomUUID().toString();
        }
        FileUtils.copyFile(Objects.requireNonNull(multipartFileToFile(file)), new File(targetPath, filename));
    }


    @Override
    public String createDirectory(String filePath, String directoryName) {
        if (!StringUtils.hasText(directoryName)) {
            throw new BizException("文件名不可为空");
        }
        if (this.checkFileExisted(filePath, directoryName)) {
            throw new BizException("文件夹已存在");
        }
        if (StringUtils.hasText(filePath)) {
            if (new File(filePath + File.separatorChar + directoryName).mkdir()) {
                return "文件夹创建成功";
            }
        } else {
            if (new File(this.getFilePath() + File.separatorChar + directoryName).mkdir()) {
                return "文件夹创建成功";
            }
        }
        return "文件夹创建失败";
    }

    @Override
    public String removeFile(FileVo fileVo) {
        if (Objects.nonNull(fileVo) && StringUtils.hasText(fileVo.getFilePath())) {
            log.info("fileVo:{}", fileVo);
            fileVo.setFilePath(fileVo.getFilePath().replace("\\", File.separator));
            File file = new File(fileVo.getFilePath());
            if (file.exists() && file.isDirectory()) {
                final File[] files = file.listFiles();
                if (Objects.nonNull(files)) {
                    for (File tFile : files) {
                        if (tFile.getName().equals(fileVo.getFileName())) {
                            if ((TYPE_FILE == fileVo.getType() && tFile.isFile())) {
                                try {
                                    Files.delete(tFile.toPath());
                                    return "删除成功";
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                return "删除失败";
                            } else if (TYPE_FOLDER == fileVo.getType() && tFile.isDirectory()) {
                                // 文件夹递归删除
                                if (delFiles(tFile)) {
                                    return "删除成功";
                                }
                                return "删除失败";
                            }
                        }
                    }
                }
            } else {
                log.error("file nonexistence");
            }
        }
        return "删除失败";
    }

    @Override
    public void view(String filePath, HttpServletResponse response) {
        log.info(filePath);
        File file = new File(filePath.replace("\\", File.separator));
        // PDF文件地址
        if (file.exists()) {
            try (FileInputStream input = new FileInputStream(file)) {
                byte[] data = new byte[input.available()];
                int read = input.read(data);
                log.info("{}", read);
                response.getOutputStream().write(data);
            } catch (Exception e) {
                // do nothing
            }
        }

    }

    @Override
    public ImgVo storeImgFile(MultipartFile file) throws IOException {
        String targetPath = this.getFilePath() + File.separatorChar + "img";
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            throw new BizException("错误的文件名");
        }
        FileUtils.copyFile(Objects.requireNonNull(multipartFileToFile(file)), new File(targetPath, filename));
        ImgVo imgVo = new ImgVo();
        imgVo.setSrc("/file/view?filePath=" + targetPath + File.separatorChar + filename);
        imgVo.setTitle(filename);
        return imgVo;
    }

    @Override
    public int saveEdit(String content) {
        EditVo editVo = new EditVo();
        editVo.setId(IdUtil.getSnowflake(0, 0).nextIdStr());
        editVo.setContent(content);
        editVo.setUpdateTime(new Date());
        //SerializeUtil.serialize(editVo);
        return editMapper.insert(editVo);
    }

    @Override
    public EditVo getEdit() {
        LambdaQueryWrapper<EditVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(EditVo::getUpdateTime).last("limit 1");
        final EditVo editVo = editMapper.selectOne(queryWrapper);
        return Objects.isNull(editVo) ? new EditVo() : editVo;
    }

    @Override
    public List<EditVo> listEdit() {
        LambdaQueryWrapper<EditVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(EditVo::getUpdateTime);
        return editMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<EditVo> pageEdit(Long page, Long limit) {
        LambdaQueryWrapper<EditVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(EditVo::getUpdateTime);
        return editMapper.selectPage(new Page<>(page, limit), queryWrapper);
    }

    @Override
    public void removeEdit(String id) {
        editMapper.deleteById(id);
    }

    private boolean checkFileExisted(String filePath, String directoryName) {
        if (StringUtils.hasText(filePath)) {
            return new File(filePath + File.separatorChar + directoryName).exists();
        }
        return new File(this.getFilePath() + File.separatorChar + directoryName).exists();
    }


    /**
     * 将MultipartFile转换为File
     *
     * @param multiFile /
     * @return /
     */
    public static File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复, 能够在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private String getFilePath() {
        if (StringUtils.hasText(fileRootPath)) {
            return fileRootPath + File.separator + "file";
        }
        return System.getProperty("user.dir") + File.separator + "file";
    }


    /**
     * 递归删除某个目录及目录下所有的子文件和子目录
     *
     * @param file 文件或者目录
     * @return 删除结果
     */
    public static boolean delFiles(File file) {
        if (file.isDirectory()) {
            File[] childrenFiles = file.listFiles();
            if (childrenFiles != null) {
                for (File childFile : childrenFiles) {
                    if (!delFiles(childFile)) {
                        return false;
                    }
                }
            }
        }
        //删除文件、空目录
        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static String getCreateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }


    public static String getFileCreateTime(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileTime t = null;
        try {
            t = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(t)) {
            return dateFormat.format(t.toMillis());
        }
        return "-";
    }


}
