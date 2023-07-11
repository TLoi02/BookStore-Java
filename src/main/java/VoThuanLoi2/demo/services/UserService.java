package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Role;
import VoThuanLoi2.demo.entity.User;
import VoThuanLoi2.demo.respository.RoleRepository;
import VoThuanLoi2.demo.respository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public void add(User newUser) {
        userRepository.save(newUser);
    }
    @Transactional
    public void addUserWithRole(User user, String nameRole) {
        User savedUser = userRepository.save(user);

        //Find role equal props handle
        Role role = roleRepository.findByName(nameRole.toUpperCase());

        savedUser.getRoles().add(role);
        userRepository.save(savedUser);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
    public User getUser(String username){
        return  userRepository.getUserByUsernameOrEmail(username);
    }
    public User getUserById(Integer id){
        return userRepository.getUserByID(id);
    }
    public List<User> getUsersByRoleId(Integer roleId) {
        return userRepository.findByRoleId(roleId);
    }
}
