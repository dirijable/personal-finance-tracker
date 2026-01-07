package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

// TODO: удалить List<Account> из User для перехода на одностороннюю связь через AccountRepository.findAllByUserId().
// TODO: удалить List<Category> из User для перехода на одностороннюю связь через CategoryRepository.findAllByUserId().

@Entity
@Table(name = "users")
@ToString(exclude = {"categories", "accounts", "password"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true,
            nullable = false,
            length = 64)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false,
            length = 32)
    String username;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Category> categories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Account> accounts = new ArrayList<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setUser(this);
    }
}
