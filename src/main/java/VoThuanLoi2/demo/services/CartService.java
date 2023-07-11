package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.*;
import VoThuanLoi2.demo.models.CartItem;
import VoThuanLoi2.demo.respository.OrderRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void clearCart() {
        cartItems.clear();
    }

    public void addToCart(Book book, int quantity) {

        CartItem findCart = cartItems.stream()
                .filter(item -> item.getId().equals(book.getId()))
                .findFirst().orElse(null);
        //have product in cart
        if(findCart != null)
        {
            findCart.setQuantity(findCart.getQuantity()+quantity);
        }
        //cart doesn't have product
        else
        {
            System.out.print("case item = null");
            findCart = new CartItem();
            findCart.setQuantity(1);

            findCart.setId(book.getId());
            findCart.setTitle(book.getTitle());
            findCart.setAuthor(book.getAuthor());
            findCart.setImage(book.getImage());
            findCart.setPrice(book.getPrice());
            findCart.setYear(book.getYear());
            findCart.setQuantityStore(book.getQuantity());

            cartItems.add(findCart);
        }
    }
    public void updateCartItem(Long productId, int quantity) {
        CartItem findCart = cartItems.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst().orElse(null);
        if(findCart != null)
        {
            findCart.setQuantity(quantity);
        }
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(cartItem -> cartItem.getId().equals(productId));
    }


    @Autowired
    private OrderRespository orderRepository;

    //Calc total money of cart
    private long calcTotalMoney(){
        return cartItems.stream()
                .mapToLong(cartItem -> (long) (cartItem.getPrice() * cartItem.getQuantity()))
                .sum();
    }

    @Autowired
    private BookService bookService;

    @Transactional
    public void orderCart(User user) {
        // Create a new Order
        Orders order = new Orders();
        order.setOrder_date(new Date());
        order.setIsPaid(false);
        order.setUser(user);
        order.setTotalAmount(calcTotalMoney());

        //Type order
        order.setType(new Type((long)1));

        // Iterate over cart items and create OrderDetails
        List<OrdersDetail> listOrderDetail = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrdersDetail orderDetail = new OrdersDetail();

            orderDetail.setOrder(order);
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setIdProduct(cartItem.getId());

            //handel quantity book in store
            bookService.orderHandel(cartItem.getId(), cartItem.getQuantity());

            listOrderDetail.add(orderDetail);
        }

        // Set order details in the order
        order.setOrderDetails(listOrderDetail);

        // Save the order to the database
        orderRepository.save(order);

        // Clear the cart
        clearCart();
    }

    public int getCartCount() {
        return cartItems.size();
    }

}
