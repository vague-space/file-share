package com.example.fileshare.task;

import com.example.fileshare.common.SessionManager;
import com.example.fileshare.vo.OnlineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author vague 6/5/2022 上午 9:42
 */
@Component
@Slf4j
public class SessionClearTask {


    /**
     * 添加定时任务
     * 每 10s 执行一次
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void configureTasks() {
        final ConcurrentMap<String, OnlineUser> userPool = SessionManager.getUserPool();
        for (Map.Entry<String, OnlineUser> entry : userPool.entrySet()) {
            if (Objects.nonNull(entry.getValue())) {
                //结束时间 转为 Long 类型
                long end = new Date().getTime();
                // 时间差 = 结束时间 - 开始时间，这样得到的差值是毫秒级别
                long timeLag = end - entry.getValue().getLastLoginTime().getTime();
                //天
                long day = timeLag / (24 * 60 * 60 * 1000);
                //小时
                long hour = (timeLag / (60 * 60 * 1000) - day * 24);
                //分钟
                long minute = ((timeLag / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (minute >= 5) {
                    log.info("用户ip:{} 已过期", entry.getValue().getIp());
                    SessionManager.remove(entry.getValue().getIp());
                }
            }
        }
        log.debug("定时任务执行完毕");
    }




}
