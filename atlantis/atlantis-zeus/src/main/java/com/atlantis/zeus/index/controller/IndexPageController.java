package com.atlantis.zeus.index.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面控制器
 * 返回值不以报体返回
 *
 * @author likang02@corp.netease.com
 * @date 2023/5/9 16:15
 */
@Slf4j
@RequestMapping("/page")
@Controller
public class IndexPageController {
    /**
     * 指定 页面地址和视图解析器
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView indexPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("msg", "req successful!!!");

        return modelAndView;
    }
}
