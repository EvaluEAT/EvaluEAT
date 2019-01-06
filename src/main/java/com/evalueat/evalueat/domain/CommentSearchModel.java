package com.evalueat.evalueat.domain;

import java.io.Serializable;

public class CommentSearchModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;
    private Integer type;
    private Integer parentId;
    private String evaluatableType;
    private Long evaluatableId;

    public String getValue() {
        return value;
    }

    public CommentSearchModel value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public CommentSearchModel type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public CommentSearchModel parentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getEvaluatableType() {
        return evaluatableType;
    }

    public CommentSearchModel evaluatableType(String evaluatableType) {
        this.evaluatableType = evaluatableType;
        return this;
    }

    public void setEvaluatableType(String evaluatableType) {
        this.evaluatableType = evaluatableType;
    }

    public Long getEvaluatableId() {
        return evaluatableId;
    }

    public CommentSearchModel evaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
        return this;
    }

    public void setEvaluatableId(Long evaluatableId) {
        this.evaluatableId = evaluatableId;
    }

    @Override
    public String toString() {
        return "Comment{" +
            ", value='" + getValue() + "'" +
            ", type=" + getType() +
            ", parentId=" + getParentId() +
            ", evaluatableType='" + getEvaluatableType() + "'" +
            ", evaluatableId=" + getEvaluatableId() +
            "}";
    }
}
