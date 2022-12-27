package com.example.fileshare.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vague 2022/5/2 19:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_edit")
public class EditVo implements Serializable {

    @TableId
    private String id;

    private String content;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

}
