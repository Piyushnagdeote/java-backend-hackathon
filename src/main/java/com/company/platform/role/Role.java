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
@SQLDelete(sql = "UPDATE roles SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = 0")
public class Role extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "BIT(1)")
    private boolean deleted = false;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
