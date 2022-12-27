package com.example.fileshare.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author vague 5/5/2022 下午 5:56
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private String ip;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

}
