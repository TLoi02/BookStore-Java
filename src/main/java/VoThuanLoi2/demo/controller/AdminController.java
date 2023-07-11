package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.*;
import VoThuanLoi2.demo.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private JobService jobService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TypeService typeService;
    private final FileUpload fileUpload;

    @GetMapping("/dashboard")
    public String index(Model model, Long idCategory){
        model.addAttribute("listCategory", categoryService.getAllCategories());

        if(idCategory == null)
            model.addAttribute("listBook", bookService.getAll());
        if(idCategory != null)
            model.addAttribute("listBook", bookService.getListBookWitdIdCategory(idCategory));


        model.addAttribute("listBlog", blogService.getAll());
        model.addAttribute("listJob", jobService.getAll());
        model.addAttribute("listApply",applyService.getAll());
        model.addAttribute("listUser",userService.getUsersByRoleId(1));
        model.addAttribute("listOrder",orderService.getListAdmin());
        model.addAttribute("listOrderSuccess",orderService.getOrderWithNameType("Thành công"));
        model.addAttribute("listOrderUnSuccess",orderService.getOrderWithNameType("Hủy"));

        return "admin/index";
    }

    @GetMapping("/book/category/{id}")
    public String handelIndex(@PathVariable("id") Long id, Model model){
        index(model, id);
        return "admin/index";
    }

    //Handel category
    @GetMapping("/category/create")
    public String createCategory(Model model){
        model.addAttribute("category",new Category());
        return "admin/category/create";
    }
    @PostMapping("/category/create")
    public String createCategory(@Valid Category newCategory,
                                 BindingResult result,
                                 Model model){
        if (result.hasErrors()){
            return "admin/category/create";
        }
        categoryService.saveCategory(newCategory);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){
        Category category = categoryService.getCategoryById(id);
        if (category == null){
            return "not-found";
        }
        model.addAttribute("category", category);
        return "admin/category/edit";
    }
    @PostMapping("/category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, @Valid @ModelAttribute("category") Category updatedCategory, BindingResult result, Model model){
        if (result.hasErrors()){
            return "admin/category/edit";
        }
        Category category = categoryService.getCategoryById(id);
        if (category == null){
            return "error";
        }
        category.setName(updatedCategory.getName());
        categoryService.saveCategory(category);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/dashboard";
    }


    //Handel Book
    @GetMapping("/book/create")
    public String createBook(Model model){
        model.addAttribute("book",new Book());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "admin/book/create";
    }
    @PostMapping("/book/create")
    public String createBook(@Valid Book newBook,
                         @RequestParam MultipartFile imageProduct,
                         BindingResult result,
                         Model model) throws IOException {
        if (result.hasErrors()){
            model.addAttribute("categories",categoryService.getAllCategories());
            return "admin/book/create";
        }
        if (imageProduct != null && imageProduct.getSize() > 0){
            String imageURL = fileUpload.uploadFile(imageProduct);
            newBook.setImage(imageURL);
        }

        bookService.add(newBook);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model){
        Book book = bookService.getBookById(id);
        if (book == null){
            return "not-found";
        }
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("book", book);
        return "admin/book/edit";
    }
    @PostMapping("/book/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                             @RequestParam MultipartFile imageProduct,
                             @Valid @ModelAttribute("book") Book updatedBook,
                             BindingResult result,
                             Model model) throws IOException {
        if (result.hasErrors()){
            model.addAttribute("categories",categoryService.getAllCategories());
            return "admin/book/edit";
        }

        Book book = bookService.getBookById(id);
        if (book == null){
            return "error";
        }
        if (imageProduct != null && imageProduct.getSize() > 0){
            String imageURL = fileUpload.uploadFile(imageProduct);
            book.setImage(imageURL);
        }

        //Set content update
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPrice(updatedBook.getPrice());
        book.setIntro(updatedBook.getIntro());
        book.setContent(updatedBook.getContent());
        book.setYear(updatedBook.getYear());
        book.setCompany(updatedBook.getCompany());
        book.setSize(updatedBook.getSize());
        book.setType(updatedBook.getType());
        book.setPages(updatedBook.getPages());
        book.setQuantity(updatedBook.getQuantity());
        book.setCategory(updatedBook.getCategory());

        bookService.updateBook(book);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookService.deleteBook(id);
        return "redirect:/admin/dashboard";
    }


    //Handel blog
    @GetMapping("/blog/create")
    public String createBlog(Model model){
        model.addAttribute("blog",new Blog());
        return "admin/blog/create";
    }
    @PostMapping("/blog/create")
    public String createBlog(@Valid Blog newBlog,
                         @RequestParam MultipartFile imageBlog,
                         BindingResult result,
                         Model model) throws IOException {
        if (result.hasErrors()){
            return "admin/blog/create";
        }

        if (imageBlog != null && imageBlog.getSize() > 0){
            String imageURL = fileUpload.uploadFile(imageBlog);
            newBlog.setImage(imageURL);
        }

        //Set date post is day create blog
        newBlog.setDatepost(LocalDate.now());

        blogService.add(newBlog);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/blog/edit/{id}")
    public String editBlog(@PathVariable("id") Long id, Model model){
        Blog blog = blogService.getBlogById(id);
        if (blog == null){
            return "not-found";
        }
        model.addAttribute("blog", blog);
        return "admin/blog/edit";
    }
    @PostMapping("/blog/edit/{id}")
    public String editBlog(@PathVariable("id") Long id,
                           @RequestParam MultipartFile imageBlog,
                           @Valid @ModelAttribute("blog") Blog updateBlog,
                           BindingResult result,
                           Model model) throws IOException {
        if (result.hasErrors()){
            model.addAttribute("categories",categoryService.getAllCategories());
            return "admin/blog/edit";
        }
        Blog blog = blogService.getBlogById(id);
        if (blog == null){
            return "error";
        }
        if (imageBlog != null && imageBlog.getSize() > 0){
            String imageURL = fileUpload.uploadFile(imageBlog);
            blog.setImage(imageURL);
        }
        blog.setTitle(updateBlog.getTitle());
        blog.setIntro(updateBlog.getIntro());
        blog.setContent(updateBlog.getContent());
        blogService.add(blog);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/blog/delete/{id}")
    public String deleteBlog(@PathVariable("id") Long id){
        blogService.deleteBlog(id);
        return "redirect:/admin/dashboard";
    }


    //Handel job
    @GetMapping("/job/create")
    public String createJob(Model model){
        model.addAttribute("job",new Job());
        return "admin/job/create";
    }
    @PostMapping("/job/create")
    public String createJob(@Valid Job newJob,
                                 BindingResult result,
                                 Model model){
        if (result.hasErrors()){
            return "admin/job/create";
        }
        jobService.saveJob(newJob);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/job/edit/{id}")
    public String editJob(@PathVariable("id") Long id, Model model){
        Job job = jobService.getJobById(id);
        if (job == null){
            return "not-found";
        }
        model.addAttribute("job", job);
        return "admin/job/edit";
    }
    @PostMapping("/job/edit/{id}")
    public String editJob(@PathVariable("id") Long id, @Valid @ModelAttribute("job") Job updateJob, BindingResult result, Model model){
        if (result.hasErrors()){
            return "admin/job/edit";
        }
        Job job = jobService.getJobById(id);
        if (job == null){
            return "error";
        }
        job.setTitle(updateJob.getTitle());
        job.setContent(updateJob.getContent());
        jobService.saveJob(job);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/job/delete/{id}")
    public String deleteJob(@PathVariable("id") Long id){
        jobService.deleteJob(id);
        return "redirect:/admin/dashboard";
    }


    //Handel apply
    @GetMapping("/apply/download/{id}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable("id") Long id) throws IOException {
        Apply apply = applyService.findById(id);

        if (apply == null || apply.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        File imageFile = new File(apply.getImage());

        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    //Handel orders
    @GetMapping("/order/update/{id}")
    public String handelUpdateOrder(@PathVariable("id") Long id, Model model){
        model.addAttribute("order",orderService.getOrderById(id));
        model.addAttribute("listType",typeService.getAll());

        return "admin/order/edit";
    }
    @PostMapping("/order/update/{id}")
    public String handelUpdate(@PathVariable("id") Long id, @ModelAttribute("order") Orders updateOrder){
        //Get order
        Orders order = orderService.getOrderById(id);

        order.setType(updateOrder.getType());
        orderService.updateOrder(order);

        return "redirect:/admin/dashboard";
    }

    private User getUSer(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();

                // Retrieve user from the database
                User user = userService.getUser(username);
                return user;
            }
        }
        return null;
    }

    @GetMapping("/order/detail/{id}")
    public String handelDetail(@PathVariable("id") Long id,
                               Model model,
                               Authentication authentication){
        User user = getUSer(authentication);

        model.addAttribute("order",orderService.getOrderById(id));
        model.addAttribute("user", user);
        model.addAttribute("listOrderDetail", orderService.getListCartWithId(id));
        model.addAttribute("pageType","admin");

        return "invoice";
    }


}
