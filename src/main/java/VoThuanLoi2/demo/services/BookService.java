package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Book;
import VoThuanLoi2.demo.respository.IBookRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private IBookRespository repo;

    public void add(Book c){
        repo.save(c);
    }
    public List<Book> getAll(){
        return (List<Book>) repo.findAll();
    }

    public List<Book> getListBookWithLimit(int limit) {
        List<Book> allBook = repo.findAll();
        int endIndex = Math.min(allBook.size(), limit);
        return allBook.subList(0, endIndex);
    }

    public List<Book> getListBookWitdIdCategory(Long id){
        return repo.findByCategoryId(id);
    }


    public Page<Book> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,8);
        return repo.findAll(pageable);
    }

    public Page<Book> findPageByIdCategory(Long id, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        List<Book> books = repo.findByCategoryId(id);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        return new PageImpl<>(books.subList(start, end), pageable, books.size());
    }


    public void deleteBook(Long id){
        repo.deleteById(id);
    }

    public Book getBookById(Long bookId) {
        return repo.findById(bookId).orElse(null);
    }

    public List<Book> getListBookByCategory(Long categoryId, Long idBookDetai){
        //filter if have book same category set limit is 4
        List<Book> filter = repo.findByCategoryId(categoryId);

        //delete book in detail page
        Book findBookDetail = null;
        for (Book book : filter) {
            if (book.getId().equals(idBookDetai)) {
                findBookDetail = book;
                break;
            }
        }
        filter.remove(findBookDetail);

        if (filter.size() > 0){
            if (filter.size() > 4)
                filter = filter.subList(0, 4);
        }
        return filter;
    }

    public void updateBook(Book b) {
        repo.save(b);
    }

    public Integer getQuantityBook(Long id){
        Integer result = 0;
        Book find = repo.findById(id).orElse(null);
        if (find != null)
            result = find.getQuantity();
        return result;
    }

    public void orderHandel(Long id, int quantity){
        Book findBook = repo.findById(id).orElse(null);
        if (findBook != null){
            Integer getValue = findBook.getQuantity();
            Integer handel = getValue - (Integer) quantity;
            findBook.setQuantity(handel);
        }
        repo.save(findBook);
    }

    public String getTitleBookWithId(Long id){
        return getBookById(id).getTitle();
    }
}
