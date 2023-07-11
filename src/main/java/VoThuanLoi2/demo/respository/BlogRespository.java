package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRespository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b ORDER BY b.datepost DESC")
    List<Blog> findLatestBlogs(int numberOfBlogs);
}
