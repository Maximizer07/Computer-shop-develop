package com.example.demo.User;

import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Review.Review;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.example.demo.WishItem.WishItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * сущность пользователя
 * @author mike
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {
    /**
     * id пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * имя пользователя
     */
    private String name;
    /**
     * фамилия пользователя
     */
    private String surname;
    /**
     * почта пользователя
     */
    private String email;
    /**
     * пароль пользователя
     */
    private String password;
    /**
     * роль пользователя
     */
    private UserRole userRole;
    /**
     * доступность аккаунта
     */
    @Builder.Default
    private Boolean locked = false;
    /**
     * активированность аккаунта
     */
    @Builder.Default
    private Boolean enabled = false;
    /**
     * пароль
     */
    @Transient
    private String password2;
    /**
     * список желаемых товаров
     */
    @OneToMany(mappedBy="user")
    private List<WishItem> wishItems;
    /**
     * список отзывов
     */
    @OneToMany(mappedBy="user")
    private List<Review> reviews;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    /**
     * @return пароль
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return почта пользователя
     */
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return заблокированность аккаунта
     */
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return доступность аккаунта
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}