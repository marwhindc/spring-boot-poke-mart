package com.springbootpokemart.springbootpokemart.user;

import com.springbootpokemart.springbootpokemart.cart.Cart;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String username;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<Cart> carts = new HashSet<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Collection<Cart> getCarts() {
        return carts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCarts(Collection<Cart> carts) {
        this.carts = carts;
    }

    public Integer getSize() {
        return this.carts.size();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", carts=" + carts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username) && Objects.equals(carts, user.carts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, carts);
    }
}
