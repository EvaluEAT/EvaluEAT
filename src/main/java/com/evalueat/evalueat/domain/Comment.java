package com.evalueat.evalueat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.evalueat.evalueat.domain.enumeration.EvaluatableType;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "relation_id")
    private Integer relationId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluatable_type")
    private EvaluatableType evaluatableType;

    @Column(name = "evaluatable_id")
    private Long evaluatableId;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Comment parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Comment value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public Comment type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public Comment relationId(Integer relationId) {
        this.relationId = relationId;
        return this;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Comment createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Comment createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Comment updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Comment deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public EvaluatableType getEvaluatableType() {
        return evaluatableType;
    }

    public Comment evaluatableType(EvaluatableType evaluatableType) {
        this.evaluatableType = evaluatableType;
        return this;
    }

    public void setEvaluatableType(EvaluatableType evaluatableType) {
        this.evaluatableType = evaluatableType;
    }

    public Long getEvaluatableId() {
        return evaluatableId;
    }

    public Comment evaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
        return this;
    }

    public void setEvaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
    }

    public Comment getParent() {
        return parent;
    }

    public Comment parent(Comment comment) {
        this.parent = comment;
        return this;
    }

    public void setParent(Comment comment) {
        this.parent = comment;
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
        Comment comment = (Comment) o;
        if (comment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", type=" + getType() +
            ", relationId=" + getRelationId() +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", evaluatableType='" + getEvaluatableType() + "'" +
            ", evaluatableId=" + getEvaluatableId() +
            "}";
    }
}
