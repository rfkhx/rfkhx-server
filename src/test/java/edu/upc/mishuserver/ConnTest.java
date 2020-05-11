package edu.upc.mishuserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.upc.mishuserver.dto.User;
import edu.upc.mishuserver.repositories.UserRepository;

@SpringBootTest
public class ConnTest {
    @Autowired
    UserRepository userDao;

    @Test
    void Test1(){
        final User user1 = new User();
        user1.setUsername("admin");
        user1.setUserpassword("admin");
        userDao.save(user1);

        final User user2 = new User();
        // user2.setId((long) 2);
        user2.setUsername("admin1");
        user2.setUserpassword("admin1");
        userDao.save(user2);

        final User user3 = new User();
        //user3.setId((long) 3);
        user3.setUsername("admin2");
        user3.setUserpassword("admin2");
        userDao.save(user3);
    }
    
    @Autowired
    UserRepository userDao1;
    @Test
    void findOne1(){

        System.out.println("数据库读写，查找函数测试");

        System.out.println(userDao1.findByUsername("admin").toString());
    }
}