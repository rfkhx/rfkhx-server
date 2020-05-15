package edu.upc.mishuserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.dto.UserDto;
import edu.upc.mishuserver.repositories.PrivilegeRepository;
import edu.upc.mishuserver.repositories.RoleRepository;
import edu.upc.mishuserver.repositories.UserRepository;
import edu.upc.mishuserver.services.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @RequestMapping("signup")
    public String signup() {
        return "user/signup";
    }

    @RequestMapping("signupHandle")
    public String signupHandle(UserDto userDto) {
        System.out.println(userDto);
        userService.registerNewUserAccount(userDto);
        return "redirect:/login";
    }
}