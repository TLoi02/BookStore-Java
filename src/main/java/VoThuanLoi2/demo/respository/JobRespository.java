package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRespository extends JpaRepository<Job, Long> {
}
