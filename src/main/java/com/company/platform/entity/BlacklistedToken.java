package com.company.platform.entity;

import com.company.platform.common.entity.BaseEntity;
import jakarta.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
@SQLDelete(sql = "UPDATE blacklisted_tokens SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class BlacklistedToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiry;

    public BlacklistedToken() {}

    public BlacklistedToken(String token, LocalDateTime expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    // ===== SETTERS =====
    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
