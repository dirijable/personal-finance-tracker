package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,
            length = 500)
    String name;

    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    User user;
}
