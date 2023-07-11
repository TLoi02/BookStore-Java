package VoThuanLoi2.demo.models;

import lombok.Data;

@Data
public class CartItem {

    //Book info
    private Long id;
    private String title;
    private String author;
    private String image;
    private Integer year;

    private Double price;

    //Quantity
    private int quantity;

    //Quantity book have in store
    private Integer quantityStore;

    public long getAmount() {
        return (long) ((long) quantity * price);
    }
}
