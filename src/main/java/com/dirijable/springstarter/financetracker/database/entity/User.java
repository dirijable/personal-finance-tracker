package com.dirijable.springstarter.financetracker.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
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
public class User extends AuditingEntity implements UserDetails{

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
    String name;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
