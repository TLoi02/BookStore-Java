package VoThuanLoi2.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="apply")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "Không được để trống họ và tên")
    private String name;

    @Column(name = "email")
    @NotNull(message = "Không được để trống email")
    private String email;

    @Column(name = "phone")
    @NotNull(message = "Không được để trống số điện thoại")
    private String phone;

    @Column(name ="image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
