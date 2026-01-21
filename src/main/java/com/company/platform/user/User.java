package com.company.platform.user;

import com.company.platform.common.entity.BaseEntity;
import com.company.platform.role.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class User extends BaseEntity {

    // ================= BASIC FIELDS =================

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private int failedAttempts = 0;

    private LocalDateTime lockTime;

    // ================= ROLE MAPPING (FIXED) =================

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",               // ✅ matches DB
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")  // ✅ matches DB
    )
    private Set<Role> roles = new HashSet<>();

    // ================= GETTERS =================

    public Long getId() {
        return super.getId();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    // ================= SETTERS =================

    public void setId(Long id) {
        super.setId(id);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
