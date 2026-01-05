package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String email;

    String password;
    String username;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Category> categories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Account> accounts = new ArrayList<>();

    public void addCategory(Category category){
        this.categories.add(category);
        category.setUser(this);
    }

    public void addAccount(Account account){
        this.accounts.add(account);
        account.setUser(this);
    }
}
