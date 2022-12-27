package com.example.fileshare.common;

/**
 * @author vague 2021/9/30 15:34
 */
public interface BaseErrorInfoInterface {

    /**
     * 错误码
     * @return code
     */
    int getResultCode();

    /**
     * 错误描述
     * @return message
     */
    String getResultMsg();

}
