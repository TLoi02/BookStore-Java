package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrdersDetail, Long> {
    List<OrdersDetail> findByOrderId(Long orderId);
}
