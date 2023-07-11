package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.Blog;
import VoThuanLoi2.demo.entity.Book;
import VoThuanLoi2.demo.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/list/{pageNumber}")
    public String getOnPage(Model model, @PathVariable("pageNumber") int currentPage){
        Page<Blog> page = blogService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Blog> blogs = page.getContent();

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listBlog", blogs);

        return "blog/index";
    }
    @GetMapping("/list")
    public String getAllPages(Model model){
        return getOnPage(model, 1);
    }

    @GetMapping("/detail/{id}")
    public String handelDetailPage(Model model, @PathVariable("id") Long id){
        Blog findBlog = blogService.getBlogById(id);
        if (findBlog == null){
            return "error";
        }

        model.addAttribute("blog", findBlog);

        return "blog/detail";
    }
}
