package edu.upc.mishuserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.repositories.AppBinaryRepository;
import edu.upc.mishuserver.utils.StringConfigUtil;

/**
 * IndexPageController
 */
@Controller
@RequestMapping("/")
public class IndexPageController {

    @Autowired
    private AppBinaryRepository appBinaryRepository;

    @RequestMapping
    String indexPage(Model model) {
        model.addAttribute("beian", StringConfigUtil.getConfig("beian", "备案信息配置丢失！"));


        
        model.addAttribute("link_win64", appBinaryRepository.findFirstByPlatformOrderByVersioncode("Win64"));
        model.addAttribute("link_linux64", appBinaryRepository.findFirstByPlatformOrderByVersioncode("Linux64"));
        model.addAttribute("link_android", appBinaryRepository.findFirstByPlatformOrderByVersioncode("Android"));
        return "index";
    }
}