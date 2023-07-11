package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRespository extends JpaRepository<Category, Long>{
}
