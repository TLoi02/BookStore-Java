package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRespository extends JpaRepository<Orders, Long> {
    List<Orders> findByType_Name(String typeName);
    List<Orders> findByUserUsername(String username);
    List<Orders> findByType_NameNotIn(List<String> excludedTypeNames);
}
