package com.dirijable.springstarter.financetracker.database.entity;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
public abstract class BaseEntity {


}
