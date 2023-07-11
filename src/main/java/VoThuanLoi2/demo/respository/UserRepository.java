package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username or u.email = :username")
    public User getUserByUsernameOrEmail(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.user_id = :user_id")
    public User getUserByID(@Param("user_id") Integer user_id);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.role_id = :roleId")
    List<User> findByRoleId(@Param("roleId") Integer roleId);

}
