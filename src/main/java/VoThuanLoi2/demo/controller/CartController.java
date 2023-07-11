package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.Book;
import VoThuanLoi2.demo.entity.User;
import VoThuanLoi2.demo.models.CartItem;
import VoThuanLoi2.demo.respository.UserRepository;
import VoThuanLoi2.demo.services.BookService;
import VoThuanLoi2.demo.services.CartService;
import VoThuanLoi2.demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private BookService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(Model model, HttpSession session) {
        //Get list item in cart
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("listCart", cartItems);

        //Handel empty or not empty cart
        String type = (cartItems.size() > 0) ? "have" : "not";
        model.addAttribute("type",type);

        //Set and handel count book in cart;
        session.setAttribute("count", cartItems.size());
        model.addAttribute("count",cartItems.size());

        //Calc total price and handel to view
        long totalPrice = cartItems.stream()
                .mapToLong(cartItem -> (long) (cartItem.getPrice() * cartItem.getQuantity()))
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        //Set date shipping
        model.addAttribute("dateOrder", LocalDate.now());
        model.addAttribute("dateFuture", LocalDate.now().plusDays(4));

        return "cart/index";
    }
    //Button add to cart on list page
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long productId) {
        //Find book click with id
        Book book = productService.getBookById(productId);
        if (book != null) {
            cartService.addToCart(book,1);
        }
        return "redirect:/cart";
    }
    //Button add to cart on detail page
    @PostMapping("/add/{id}")
    public String addToCartDetail(@PathVariable("id") Long productId,
                                  @RequestParam("quantity") int quantity
                                  ) {
        //Find book click with id
        Book book = productService.getBookById(productId);
        if (book != null) {
            cartService.addToCart(book,quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable("id") Long productId,
                                 @RequestParam("quantity") int quantity) {
        cartService.updateCartItem(productId, quantity);
        return "redirect:/cart";
    }
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
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

    @GetMapping("/checkout")
    public String Order(Model model, Authentication authentication) {
        //Get user login
        User user = getUSer(authentication);

        //Get list item in cart
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("listCart", cartItems);

        //Calc total price and handel to view
        long totalPrice = cartItems.stream()
                .mapToLong(cartItem -> (long) (cartItem.getPrice() * cartItem.getQuantity()))
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        //Set user info
        model.addAttribute("user",user);

        return "/cart/checkout";
    }
    @PostMapping("/checkout/{id}")
    public String checkout(@PathVariable("id") Integer id,
                           @ModelAttribute("user") User getUser) {
        //Get user and handel checkout
        User user = userService.getUserById(id);
        cartService.orderCart(user);

        // User info in check out form
        user.setName(getUser.getName());
        user.setPhone(getUser.getPhone());
        user.setAddress(getUser.getAddress());
        userService.saveUser(user);

        return "/cart/success";
    }
}
