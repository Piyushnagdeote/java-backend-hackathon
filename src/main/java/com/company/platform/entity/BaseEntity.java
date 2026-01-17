package com.company.platform.common.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // ================= PRIMARY KEY =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= AUDIT FIELDS =================
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    // ================= SOFT DELETE =================
    @Column(nullable = false, columnDefinition = "BIT(1)")
    private boolean deleted = false;


    // ================= OPTIMISTIC LOCK =================
    @Version
    private Long version;

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Long getVersion() {
        return version;
    }

    // ================= SETTERS =================

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // ✅ IMPORTANT: protected ID setter for child entities
    protected void setId(Long id) {
        this.id = id;
    }
}
