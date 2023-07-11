package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.Blog;
import VoThuanLoi2.demo.entity.Book;
import VoThuanLoi2.demo.services.BlogService;
import VoThuanLoi2.demo.services.BookService;
import VoThuanLoi2.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String handel(Model model){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String index(Model model){
        //props
        //Get 6 lasted blog
        List<Blog> findBlog = blogService.getLastedBlog(6);
        List<Blog> listSlide = findBlog.subList(0,2);
        List<Blog> listLasted = findBlog.subList(2,6);

        //Get 8 Product
        List<Book> listBook = bookService.getListBookWithLimit(8);
        List<Book> listFirst = new ArrayList<>();
        List<Book> listSecond = new ArrayList<>();

        //Handel sublist
        if (listBook.size() >= 4) {
            listFirst = listBook.subList(0, 4);
            if (listBook.size() >= 8) {
                listSecond = listBook.subList(4, 8);
            } else {
                listSecond = listBook.subList(4, Math.min(listBook.size(), 8));
            }
        } else {
            listFirst = listBook.subList(0, listBook.size());
        }

        model.addAttribute("listBookFirst", listFirst);
        if(listSecond.size() > 0){
            model.addAttribute("listBookSecond", listSecond);
            model.addAttribute("typeBook","have");
        }
        else{
            model.addAttribute("typeBook","not");
        }

        //List category
        model.addAttribute("listCategory", categoryService.getListWithLimit(4));

        model.addAttribute("listSlide",listSlide);
        model.addAttribute("listLasted",listLasted);

        return "home/index";
    }
}
