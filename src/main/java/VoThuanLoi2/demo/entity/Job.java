package VoThuanLoi2.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull(message = "Không được để trống tên công việc")
    private String title;

    @Column(name = "content")
    @NotNull(message = "Không được để trống nội dung")
    private String content;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Apply> applies;
}
