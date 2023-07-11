package VoThuanLoi2.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotEmpty(message = "Khong duoc de trong ten sach")
    @Size(max = 50, min =1, message = "Chuoi khong hop le")
    private String title;

    @Column(name ="image")
    private String image;

    @Column(name = "datepost")
    private LocalDate datepost;

    @Column(name = "intro", columnDefinition = "TEXT")
    @NotNull(message = "Vui lòng nhập mô tả ngắn")
    private String intro;

    @Column(name = "content", columnDefinition = "TEXT")
    @NotNull(message = "Vui lòng nhập nội dung")
    private String content;

}

