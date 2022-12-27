package com.example.fileshare.common;

import com.example.fileshare.vo.OnlineUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author vague 6/5/2022 上午 9:25
 */
@Data
public class SessionManager {

    private SessionManager(){
        // do nothing
    }

    public static ConcurrentMap<String, OnlineUser> getUserPool() {
        return USER_POOL;
    }

    private static final ConcurrentHashMap<String, OnlineUser> USER_POOL = new ConcurrentHashMap<>();


    public static void add(String key,OnlineUser user){
        USER_POOL.put(key, user);
    }


    public static void remove(String key){
        USER_POOL.remove(key);
    }

    public static void clear(){
        USER_POOL.clear();
    }


    public static OnlineUser get(String key){
        return USER_POOL.get(key);
    }


    public static List<OnlineUser> getAll(){
        return new ArrayList<>(USER_POOL.values());
    }


}
