package com.company.platform.role;

import com.company.platform.common.entity.BaseEntity;
import com.company.platform.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@SQLDelete(sql = "UPDATE roles SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Role extends BaseEntity {

    // 🔹 ID comes from BaseEntity

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // ================== GETTERS ==================

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    // ================== SETTERS ==================

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
