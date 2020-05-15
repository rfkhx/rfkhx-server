package edu.upc.mishuserver.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.upc.mishuserver.model.Privilege;
import edu.upc.mishuserver.model.Role;
import edu.upc.mishuserver.model.User;
import edu.upc.mishuserver.repositories.RoleRepository;
import edu.upc.mishuserver.repositories.UserRepository;
import edu.upc.mishuserver.services.UserService;
import edu.upc.mishuserver.utils.StringConfigUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitSetup implements ApplicationRunner {
    @Value("${app.version}")
    private String version;
    @Value("${app.build.time}")
    private String buildTime;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 在第一次启动时进行一些安装工作。
        log.debug("检查版本信息，当前version={}，buildTime={}", version, buildTime);
        String expectedVersion = StringConfigUtil.getConfig("app.version", "");
        String expectedBuildTime = StringConfigUtil.getConfig("app.buildTime", "defaultValue");
        log.debug("期望version={}，buildTime={}", expectedVersion, expectedBuildTime);
        if (!expectedVersion.equals(version)) {
            perVersion();
            StringConfigUtil.writeConfig("app.version", version);
        }
        if (!expectedBuildTime.equals(buildTime)) {
            perBuild();
            StringConfigUtil.writeConfig("app.buildTime", buildTime);
        }
    }

    /**
     * 每个版本执行一次
     */
    private void perVersion() {
        log.info("版本检查不通过，执行初始化操作。");
        Privilege readPrivilege = userService.createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = userService.createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        userService.createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        userService.createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * 每次编译执行一次
     */
    private void perBuild() {
        log.info("编译时间检查不通过，执行初始化操作。");
    }

}