package edu.upc.mishuserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.dto.UserDto;
import edu.upc.mishuserver.services.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("signup")
    public String signup() {
        return "user/signup";
    }

    @RequestMapping("signupHandle")
    public String signupHandle(UserDto userDto) {
        // System.out.println(userDto);
        if(!userDto.getPassword().equals(userDto.getMatchingPassword())){
            return "redirect:./signup";
        }
        userService.registerNewUserAccount(userDto);
        return "redirect:/login";
    }
}