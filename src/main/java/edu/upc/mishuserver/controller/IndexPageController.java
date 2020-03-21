package edu.upc.mishuserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.utils.StringConfigUtil;

/**
 * IndexPageController
 */
@Controller
@RequestMapping("/")
public class IndexPageController {

    @RequestMapping
    String indexPage(Model model){
        model.addAttribute("beian", StringConfigUtil.getConfig("beian", "备案信息配置丢失！"));
        return "index";
    }
}