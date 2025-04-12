package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories") // ✅ 실제 DB 테이블 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") // ✅ DB 필드와 정확히 매칭
    private Long categoryId;

    @Column(name = "category_name", length = 100, nullable = false)
    private String categoryName;

}
