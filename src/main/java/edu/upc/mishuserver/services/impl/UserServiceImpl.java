package edu.upc.mishuserver.services.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.upc.mishuserver.dto.UserDto;
import edu.upc.mishuserver.error.UserAlreadyExistException;
import edu.upc.mishuserver.model.Privilege;
import edu.upc.mishuserver.model.Role;
import edu.upc.mishuserver.model.User;
import edu.upc.mishuserver.repositories.PrivilegeRepository;
import edu.upc.mishuserver.repositories.RoleRepository;
import edu.upc.mishuserver.repositories.UserRepository;
import edu.upc.mishuserver.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    
    @Override
    public User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }
        final User user = new User();

        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        // user.setUsing2FA(accountDto.isUsing2FA());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);

    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Override
    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}