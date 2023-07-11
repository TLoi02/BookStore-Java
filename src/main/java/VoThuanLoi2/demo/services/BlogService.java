package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Blog;
import VoThuanLoi2.demo.respository.BlogRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRespository repo;

    public Page<Blog> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,8);
        return repo.findAll(pageable);
    }
    public Blog getBlogById(Long id){
        return repo.findById(id).orElse(null);
    }
    public List<Blog> getLastedBlog(int value){
        return repo.findLatestBlogs(value);
    }
    public List<Blog> getAll(){
        return repo.findAll();
    }
    public void add(Blog b){
        repo.save(b);
    }
    public void deleteBlog(Long id){
        repo.deleteById(id);
    }
}
