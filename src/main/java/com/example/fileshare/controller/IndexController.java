package com.example.fileshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制页面跳转
 *
 * @author vague 2022/4/29 11:40
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("")
    public String defaultPath() {
        return "file";
    }


    @RequestMapping("/{html}")
    public String index(@PathVariable String html) {
        return html;
    }


    @RequestMapping("/{directory}/{html}")
    public String index(@PathVariable String directory, @PathVariable String html) {
        return directory + "/" + html;
    }

}
