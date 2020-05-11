package edu.upc.mishuserver.controller;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.upc.mishuserver.repositories.UserRepository;

/*
*RegistController
*/
@Controller
public class RegistController {
    @Autowired
    private UserRepository userRepository;

    /*
    * 检查用户是否已经注册
    */
    public Boolean check(String username) {
        if(userRepository.findByUsername(username)==null){
            return true;//该用户名未被注册
        }
        return false;
    }

    @RequestMapping(value = "/Regist")
    public void regist(String username,String password){
        if(check(username)){
            
        }
    }

}