package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Orders;
import VoThuanLoi2.demo.entity.OrdersDetail;
import VoThuanLoi2.demo.models.CartItem;
import VoThuanLoi2.demo.respository.OrderDetailRepository;
import VoThuanLoi2.demo.respository.OrderRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRespository orderRespository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private BookService bookService;

    public Orders getOrderById(Long id){return orderRespository.findById(id).orElse(null);}

    public void updateOrder(Orders orders){orderRespository.save(orders);}

    public List<Orders> getAll(){
        return orderRespository.findAll();
    }

    public List<Orders> getListAdmin(){
        List<String> excludedTypeNames = Arrays.asList("Hoàn thành", "Hủy");
        return orderRespository.findByType_NameNotIn(excludedTypeNames);
    }

    public List<Orders> getOrdersByUserName(String userName) {
        return orderRespository.findByUserUsername(userName);
    }

    public List<OrdersDetail> getAllOrderDetailWithId(Long id){
        return orderDetailRepository.findByOrderId(id);
    }

    public List<CartItem> getListCartWithId(Long id){
        List<OrdersDetail> getDB = getAllOrderDetailWithId(id);
        List<CartItem> result = new ArrayList<>();

        for (OrdersDetail order: getDB) {
            CartItem tempt = new CartItem();
            tempt.setId(order.getIdProduct());
            tempt.setTitle(bookService.getTitleBookWithId(order.getIdProduct()));
            tempt.setQuantity(order.getQuantity());
            tempt.setPrice(order.getPrice());

            result.add(tempt);
        }

        return result;
    }

    public List<Orders> getOrderWithNameType(String nameType){
        return orderRespository.findByType_Name(nameType);
    }
}
