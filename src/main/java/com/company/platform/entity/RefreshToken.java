package com.company.platform.auth.entity;

import com.company.platform.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() { return id; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Instant getExpiryDate() { return expiryDate; }

    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
