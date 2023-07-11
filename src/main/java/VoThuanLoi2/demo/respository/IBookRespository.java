package VoThuanLoi2.demo.respository;

import VoThuanLoi2.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookRespository extends JpaRepository<Book, Long>{
    List<Book> findByCategoryId(Long categoryId);
}
