package com.example.fileshare.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vague 2022/4/29 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_file")
public class FileVo {


    @TableId
    private Long id;
    private String createName;
    private String fromIp;
    private String fileName;
    private String filePath;
    private String createTime;

    @TableField(exist = false)
    private int type;

    @TableField(exist = false)
    private String fileSize;


}
