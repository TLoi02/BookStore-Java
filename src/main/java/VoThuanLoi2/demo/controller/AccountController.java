package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.Book;
import VoThuanLoi2.demo.entity.OrdersDetail;
import VoThuanLoi2.demo.entity.User;
import VoThuanLoi2.demo.models.CartItem;
import VoThuanLoi2.demo.services.BookService;
import VoThuanLoi2.demo.services.OrderService;
import VoThuanLoi2.demo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/error")
    public String index(){
        return "account/error";
    }

    @GetMapping("/logout")
    public String logoutAccount(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        // Delete the JSESSIONID cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/home";
    }


    //Function get user login
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
    @GetMapping("/account")
    public String handleUser(Model model, Authentication authentication) {
        User user = getUSer(authentication);
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("listOrder",orderService.getOrdersByUserName(user.getUsername()));
        return "account/index";
    }

    @GetMapping("/account/update")
    public String updateAccount(Model model, Authentication authentication){
        User user = getUSer(authentication);
        if (user != null)
            model.addAttribute("user", user);

        return "account/edit";
    }
    @PostMapping("/account/update/{id}")
    public String updateAccount(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("user") User updateUser,
            BindingResult result,
            Model model
    ){
        if (result.hasErrors()){
            return "account/edit";
        }
        User user = userService.getUserById(id);
        if (user == null){
            return "error";
        }
        user.setEmail(updateUser.getEmail());
        user.setUsername(updateUser.getUsername());
        user.setName(updateUser.getName());
        user.setPhone(updateUser.getPhone());
        user.setAddress(updateUser.getAddress());

        userService.saveUser(user);
        return "redirect:/account";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }
    @PostMapping("/logout")
    public String logout() {
        return "account/logout";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "account/register";
    }
    @PostMapping("/register")
    public String handelRegister(@ModelAttribute("user") User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //set default when register account is user
        userService.addUserWithRole(user,"user");

        return "redirect:/login";
    }

    @Autowired
    private BookService bookService;
    @GetMapping("/account/order/{id}")
    public String orderDetailAccount(@PathVariable("id") Long id,
                                     Model model,
                                     Authentication authentication){
        User user = getUSer(authentication);

        model.addAttribute("order",orderService.getOrderById(id));
        model.addAttribute("user", user);
        model.addAttribute("listOrderDetail", orderService.getListCartWithId(id));
        model.addAttribute("pageType","user");

        return "invoice";
    }
}
