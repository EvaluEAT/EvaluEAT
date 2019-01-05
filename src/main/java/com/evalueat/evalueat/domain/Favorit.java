package com.evalueat.evalueat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Favorit.
 */
@Entity
@Table(name = "favorit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Favorit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluatable_type")
    private String evaluatableType;

    @Column(name = "evaluatable_id")
    private Long evaluatableId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvaluatableType() {
        return evaluatableType;
    }

    public Favorit evaluatableType(String evaluatableType) {
        this.evaluatableType = evaluatableType;
        return this;
    }

    public void setEvaluatableType(String evaluatableType) {
        this.evaluatableType = evaluatableType;
    }

    public Long getEvaluatableId() {
        return evaluatableId;
    }

    public Favorit evaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
        return this;
    }

    public void setEvaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Favorit createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Favorit createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Favorit updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Favorit deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorit favorit = (Favorit) o;
        if (favorit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favorit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Favorit{" +
            "id=" + getId() +
            ", evaluatableType='" + getEvaluatableType() + "'" +
            ", evaluatableId=" + getEvaluatableId() +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
