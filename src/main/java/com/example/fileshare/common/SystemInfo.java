package com.example.fileshare.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author vague 2022/5/3 14:00
 */
@Data
@Accessors(chain = true)
public class SystemInfo {

    /**
     * 操作系统名称
     */
    private String osName;

    /**
     * 操作系统构架
     */
    private String osArch;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * 运行时环境版本
     */
    private String javaVersion;

    /**
     * Java 安装目录
     */
    private String javaHome;

    /**
     * 文件分隔符（在 UNIX 系统中是“/”）
     */
    private String fileSeparator;

    /**
     * 路径分隔符（在 UNIX 系统中是“:”）
     */
    private String pathSeparator;

    /**
     * 行分隔符（在 UNIX 系统中是“/n”）
     */
    private String lineSeparator;

    /**
     * 用户的账户名称
     */
    private String userName;

    /**
     * 用户的主目录
     */
    private String userHome;

    /**
     * 用户的当前工作目录
     */
    private String userDir;
}
