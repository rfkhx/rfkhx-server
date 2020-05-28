package edu.upc.mishuserver.controller;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.upc.mishuserver.model.AppBinary;
import edu.upc.mishuserver.model.Role;
import edu.upc.mishuserver.model.User;
import edu.upc.mishuserver.repositories.AppBinaryRepository;
import edu.upc.mishuserver.repositories.RoleRepository;
import edu.upc.mishuserver.repositories.UserRepository;
import edu.upc.mishuserver.utils.StringConfigUtil;
import lombok.extern.slf4j.Slf4j;
import java.security.Principal;

/**
 * AdminController
 */
@Controller
@RequestMapping("admin")
@Slf4j
public class AdminController {
    @Autowired
    private AppBinaryRepository appBinaryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("")
    String index() {
        return "admin/index";
    }

    @RequestMapping("system")
    String systemManagePage(Model model) {
        model.addAttribute("beian", StringConfigUtil.getConfig("beian", "备案信息配置丢失！"));
        model.addAttribute("uppath", StringConfigUtil.getConfig("uppath", "/tmp/"));
        model.addAttribute("url", StringConfigUtil.getConfig("url", "http://loaclhost/"));
        return "admin/system";
    }

    @RequestMapping("systemHandler")
    String systemManageHandler(@RequestParam String beian, @RequestParam String uppath, @RequestParam String url) {
        StringConfigUtil.writeConfig("beian", beian);
        StringConfigUtil.writeConfig("uppath", uppath);
        StringConfigUtil.writeConfig("url", url);
        return "redirect:system";
    }

    @RequestMapping("publish")
    String publish(Model model) {
        model.addAttribute("binaries", appBinaryRepository.findAll());
        return "admin/publish";
    }

    @RequestMapping("publishHandler")
    String publishHandler(HttpServletRequest req, @RequestParam("file") MultipartFile file,
            @RequestParam Long versionCode, @RequestParam String versionName, @RequestParam String platform,
            @RequestParam String description) {
        String upPath = StringConfigUtil.getConfig("uppath", "/tmp/");
        String fileName = System.currentTimeMillis() + "."
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String regEx = "[\\\\\\\\/:*?\\\"<>|]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches()) {
            log.warn("用户{}尝试上传一个文件名不合法的文件{}", req.getRemoteAddr(), fileName);
            return "redirect:/";
        }
        final String[] strHex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        String destFileName = upPath + fileName;
        File destFile = new File(destFileName);
        // destFile.getParentFile().mkdirs();
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdr = messageDigest.digest(file.getBytes());
            for (int i = 0; i < mdr.length; i++) {
                int d = mdr[i];
                if (d < 0) {
                    d += 256;
                }
                int d1 = d / 16;
                int d2 = d % 16;
                sb.append(strHex[d1] + strHex[d2]);
            }
            file.transferTo(destFile);
        } catch (IllegalStateException | IOException | NoSuchAlgorithmException e) {
            log.error("文件{}上传失败，读写异常：{}", destFile.getAbsolutePath(), e.toString());
            return "redirect:/";
        }
        // fileService.addFile(fileName,
        // fileName.substring(fileName.lastIndexOf(".")+1));
        // return new Result(0,"图片上传成功！","/file/down/" + fileName);
        AppBinary appBinary = AppBinary.builder().description(description).platform(platform).versioncode(versionCode)
                .versionname(versionName).filename(fileName).md5(sb.toString()).size(file.getSize()).build();
        appBinaryRepository.save(appBinary);
        return "redirect:publish";
    }

    @RequestMapping("deleteVersionHandler")
    String deleteVersionHandler(@RequestParam Long id) {
        AppBinary appBinary = appBinaryRepository.getOne(id);
        String upPath = StringConfigUtil.getConfig("uppath", "tmp");
        if (appBinary != null) {
            File file = new File(upPath + appBinary.getFilename());
            if (file.exists()) {
                file.delete();
            }
            appBinaryRepository.delete(appBinary);
        }
        return "redirect:publish";
    }

    @RequestMapping("user")
    String userManagement(Model model, Principal principal,
            @PageableDefault(value = 10, sort = { "id" }) Pageable pageable) {
        model.addAttribute("name", principal.getName());
        model.addAttribute("users", userRepository.findAll(pageable));
        model.addAttribute("roles", roleRepository.findAll());
        Page<User> user = userRepository.findAll(pageable);
        user.previousOrFirstPageable();
        return "admin/usermanagement";
    }

    @PostMapping("delUserHandle")
    String delUserHandle(@RequestParam Long uid, Principal principal) {
        Optional<User> user = userRepository.findById(uid);
        if (!user.isEmpty() ) {
            if (principal.getName().equals(user.get().getEmail())) {
                // 不能操作自己的角色
                ;
            } else {
                userRepository.delete(user.get());
            }
        }
        return "redirect:user";
    }

    @PostMapping("addRoleHandle")
    String addRoleHandle(@RequestParam Long uid, @RequestParam Long rid, Principal principal) {
        Optional<User> user = userRepository.findById(uid);
        Optional<Role> role = roleRepository.findById(rid);
        if (!user.isEmpty() && !role.isEmpty()) {
            if (principal.getName().equals(user.get().getEmail())) {
                // 不能操作自己的角色
                ;
            } else {
                User opUser = user.get();
                Role opRole = role.get();
                if (!opUser.getRoles().contains(opRole)) {
                    opUser.getRoles().add(opRole);
                    userRepository.save(opUser);
                }
            }
        }
        return "redirect:user";
    }

    @PostMapping("delRoleHandle")
    String delRoleHandle(@RequestParam Long uid, @RequestParam Long rid, Principal principal) {
        Optional<User> user = userRepository.findById(uid);
        Optional<Role> role = roleRepository.findById(rid);
        if (!user.isEmpty() && !role.isEmpty()) {
            if (principal.getName().equals(user.get().getEmail())) {
                // 不能操作自己的角色
                ;
            } else {
                User opUser = user.get();
                Role opRole = role.get();
                log.debug("当前操作用户{}，删除角色{}", opUser.getEmail(), opRole.getName());
                if (opUser.getRoles().contains(opRole)) {
                    opUser.getRoles().remove(opRole);
                    opRole.getUsers().remove(opUser);
                    userRepository.save(opUser);
                }
            }
        }
        return "redirect:user";
    }
}