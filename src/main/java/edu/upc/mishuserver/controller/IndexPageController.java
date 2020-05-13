package edu.upc.mishuserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.model.AppBinary;
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

        AppBinary win64Binary = appBinaryRepository.findFirstByPlatformOrderByVersioncodeDesc("Win64");
        AppBinary linux64Binary = appBinaryRepository.findFirstByPlatformOrderByVersioncodeDesc("Linux64");
        AppBinary androidBinary = appBinaryRepository.findFirstByPlatformOrderByVersioncodeDesc("Android");

        if (win64Binary != null) {
            model.addAttribute("link_win64", "down/win64/" + win64Binary.getFilename());
        }
        if (linux64Binary != null) {
            model.addAttribute("link_linux64", "down/linux64/" + linux64Binary.getFilename());
        }
        if (androidBinary != null) {
            model.addAttribute("link_android", "down/android/" + androidBinary.getFilename());
        }
        return "index";
    }
}